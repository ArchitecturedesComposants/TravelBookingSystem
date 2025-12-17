package ma.emsi.oussama.bookingservice.controllers;

import ma.emsi.oussama.bookingservice.clients.HotelRestClient;
import ma.emsi.oussama.bookingservice.dtos.HotelDTO;
import ma.emsi.oussama.bookingservice.dtos.ReservationRequest;
import ma.emsi.oussama.bookingservice.dtos.VolDTO;
import ma.emsi.oussama.bookingservice.entities.Reservation;
import ma.emsi.oussama.bookingservice.enums.StatutReservation;
import ma.emsi.oussama.bookingservice.repositories.ReservationRepository;
import ma.emsi.oussama.bookingservice.services.PaiementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        // WebClient pour le vol-service. L'URI est le nom du service enregistré dans
        // Eureka.
        this.volWebClient = webClientBuilder.baseUrl("http://VOL-SERVICE").build();
    }

    @GetMapping("/reservations")
    public String test() {
        return "Booking service is running!";
    }

    @GetMapping("/reservations/search/findByClientEmail")
    public ResponseEntity<List<Reservation>> getReservationsByEmail(@RequestParam("clientEmail") String clientEmail) {
        List<Reservation> reservations = reservationRepository.findByClientEmail(clientEmail);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> creerReservation(@RequestBody ReservationRequest request) {
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
                    // Vol not available - only fail if this was the primary booking item
                    if (request.getHotelId() == null || request.getHotelId() <= 0) {
                        return ResponseEntity.badRequest().build();
                    }
                    // Otherwise continue with hotel-only booking
                    vol = null;
                }
            } catch (Exception e) {
                // If vol-service is unavailable and this is flight-only booking, fail
                if (request.getHotelId() == null || request.getHotelId() <= 0) {
                    return ResponseEntity.badRequest().build();
                }
                // Otherwise continue with hotel-only booking
                vol = null;
            }
        }

        // 2. Récupération des détails de l'Hôtel et vérification de la disponibilité
        // (via FeignClient)
        if (request.getHotelId() != null && request.getHotelId() > 0) {
            try {
                hotel = hotelRestClient.getHotelDetails(request.getHotelId());
                Boolean hotelDispo = hotelRestClient.verifierDispo(request.getHotelId(), request.getTypeChambre());

                if (hotel != null && Boolean.TRUE.equals(hotelDispo)) {
                    montantTotal = montantTotal.add(hotel.getPrixParNuit());
                } else {
                    // Hotel not available - only fail if this was the primary booking item
                    if (vol == null) {
                        return ResponseEntity.badRequest().build();
                    }
                    // Otherwise continue with flight-only booking
                    hotel = null;
                }
            } catch (Exception e) {
                // If hotel-service is unavailable and this is hotel-only booking, fail
                if (vol == null) {
                    return ResponseEntity.badRequest().build();
                }
                // Otherwise continue with flight-only booking
                hotel = null;
            }
        }

        // Ensure at least one item was successfully booked
        if (vol == null && hotel == null) {
            return ResponseEntity.badRequest().build();
        }

        // 3. Calcul du montant total déjà effectué ci-dessus

        // 4. Création de la réservation en attente de paiement
        Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setClientEmail(request.getClientEmail());
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE_PAIEMENT);
        reservation.setVolId(request.getVolId());
        reservation.setHotelId(request.getHotelId());
        reservation.setMontantTotal(montantTotal);

        // Set new booking details
        reservation.setPassengerName(request.getPassengerName());
        reservation.setTypeChambre(request.getTypeChambre());
        reservation.setSeatNumber(request.getSeatNumber());
        reservation.setFlightClass(request.getFlightClass());
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation = reservationRepository.save(reservation);

        // 5. Paiement (via PaiementService)
        try {
            String transactionId = paiementService.payer(montantTotal);

            // 6. Confirmation et mise à jour
            reservation.setTransactionPaiementId(transactionId);
            reservation.setStatutReservation(StatutReservation.CONFIRMEE);

            // TODO: Ajouter ici la logique pour décrémenter les places dans vol-service et
            // marquer la chambre comme non disponible dans hotel-service.

            return ResponseEntity.ok(reservationRepository.save(reservation));

        } catch (Exception e) {
            // 7. Annulation/Compensation en cas d'échec de paiement
            reservation.setStatutReservation(StatutReservation.ANNULEE);
            reservationRepository.save(reservation);
            // On retourne la réservation annulée avec un statut d'erreur
            return ResponseEntity.status(500).body(reservation);
        }
    }
}
