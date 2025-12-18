package ma.emsi.oussama.bookingservice.controllers;

import ma.emsi.oussama.bookingservice.clients.HotelRestClient;
import ma.emsi.oussama.bookingservice.dtos.HotelDTO;
import ma.emsi.oussama.bookingservice.dtos.ReservationRequest;
import ma.emsi.oussama.bookingservice.dtos.VolDTO;
import ma.emsi.oussama.bookingservice.entities.Reservation;
import ma.emsi.oussama.bookingservice.entities.User;
import ma.emsi.oussama.bookingservice.enums.StatutReservation;
import ma.emsi.oussama.bookingservice.repositories.ReservationRepository;
import ma.emsi.oussama.bookingservice.services.PaiementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class BookingController {

    private final ReservationRepository reservationRepository;
    private final HotelRestClient hotelRestClient;
    private final PaiementService paiementService;
    private final WebClient volWebClient;

    public BookingController(ReservationRepository reservationRepository, HotelRestClient hotelRestClient,
            PaiementService paiementService, WebClient.Builder webClientBuilder) {
        this.reservationRepository = reservationRepository;
        this.hotelRestClient = hotelRestClient;
        this.paiementService = paiementService;
        this.volWebClient = webClientBuilder.baseUrl("http://VOL-SERVICE").build();
    }

    /**
     * Get current authenticated user from SecurityContext
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Collections.singletonMap("message", "Booking service is running!"));
    }

    /**
     * Get all reservations for the current authenticated user
     */
    @GetMapping("/reservations/my")
    public ResponseEntity<List<Reservation>> getMyReservations() {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<Reservation> reservations = reservationRepository.findByUser_Id(user.getId());
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get reservation by ID - with ownership check
     */
    @GetMapping("/reservations/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable("id") Long id) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Not authenticated"));
        }

        return reservationRepository.findById(id)
                .map(reservation -> {
                    // Check ownership
                    if (reservation.getUser() == null || !reservation.getUser().getId().equals(user.getId())) {
                        return ResponseEntity.status(403).body(Collections.singletonMap("error", "Access denied"));
                    }
                    return ResponseEntity.ok(reservation);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> creerReservation(@RequestBody ReservationRequest request) {
        // Get authenticated user
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Authentication required"));
        }

        BigDecimal montantTotal = BigDecimal.ZERO;
        VolDTO vol = null;
        HotelDTO hotel = null;

        // 1. Récupération des détails du Vol (si volId est fourni)
        if (request.getVolId() != null && request.getVolId() > 0) {
            try {
                vol = volWebClient.get()
                        .uri("/vols/{id}", request.getVolId())
                        .retrieve()
                        .bodyToMono(VolDTO.class)
                        .block();

                if (vol != null && vol.getPlacesDisponibles() > 0) {
                    montantTotal = montantTotal.add(vol.getPrix());
                } else {
                    if (request.getHotelId() == null || request.getHotelId() <= 0) {
                        return ResponseEntity.badRequest()
                                .body(Collections.singletonMap("error", "Flight not available"));
                    }
                    vol = null;
                }
            } catch (Exception e) {
                if (request.getHotelId() == null || request.getHotelId() <= 0) {
                    return ResponseEntity.badRequest()
                            .body(Collections.singletonMap("error", "Flight service unavailable"));
                }
                vol = null;
            }
        }

        // 2. Récupération des détails de l'Hôtel
        if (request.getHotelId() != null && request.getHotelId() > 0) {
            try {
                hotel = hotelRestClient.getHotelDetails(request.getHotelId());
                Boolean hotelDispo = hotelRestClient.verifierDispo(request.getHotelId(), request.getTypeChambre());

                if (hotel != null && Boolean.TRUE.equals(hotelDispo)) {
                    montantTotal = montantTotal.add(hotel.getPrixParNuit());
                } else {
                    if (vol == null) {
                        return ResponseEntity.badRequest()
                                .body(Collections.singletonMap("error", "Hotel not available"));
                    }
                    hotel = null;
                }
            } catch (Exception e) {
                if (vol == null) {
                    return ResponseEntity.badRequest()
                            .body(Collections.singletonMap("error", "Hotel service unavailable"));
                }
                hotel = null;
            }
        }

        if (vol == null && hotel == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "No booking items available"));
        }

        // 3. Création de la réservation
        Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setClientEmail(user.getEmail()); // Use authenticated user's email
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE_PAIEMENT);
        reservation.setVolId(request.getVolId());
        reservation.setHotelId(request.getHotelId());
        reservation.setMontantTotal(montantTotal);
        reservation.setUser(user); // Set user ownership

        // Set booking details
        reservation.setPassengerName(request.getPassengerName());
        reservation.setTypeChambre(request.getTypeChambre());
        reservation.setSeatNumber(request.getSeatNumber());
        reservation.setFlightClass(request.getFlightClass());
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());

        reservation = reservationRepository.save(reservation);

        // 4. Paiement
        try {
            String transactionId = paiementService.payer(montantTotal);
            reservation.setTransactionPaiementId(transactionId);
            reservation.setStatutReservation(StatutReservation.CONFIRMEE);
            return ResponseEntity.ok(reservationRepository.save(reservation));
        } catch (Exception e) {
            reservation.setStatutReservation(StatutReservation.ANNULEE);
            reservationRepository.save(reservation);
            return ResponseEntity.status(500).body(reservation);
        }
    }
}
