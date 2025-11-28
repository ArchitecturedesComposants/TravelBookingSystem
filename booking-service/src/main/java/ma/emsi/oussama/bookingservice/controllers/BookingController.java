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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
public class BookingController {

    private final ReservationRepository reservationRepository;
    private final HotelRestClient hotelRestClient;
    private final PaiementService paiementService;
    private final WebClient volWebClient;

    public BookingController(ReservationRepository reservationRepository, HotelRestClient hotelRestClient, PaiementService paiementService, WebClient.Builder webClientBuilder) {
        this.reservationRepository = reservationRepository;
        this.hotelRestClient = hotelRestClient;
        this.paiementService = paiementService;
        // WebClient pour le vol-service. L'URI est le nom du service enregistré dans Eureka.
        this.volWebClient = webClientBuilder.baseUrl("http://VOL-SERVICE").build();
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> creerReservation(@RequestBody ReservationRequest request) {
        // 1. Récupération des détails du Vol (via WebClient)
        VolDTO vol = volWebClient.get()
                .uri("/vols/{id}", request.getVolId())
                .retrieve()
                .bodyToMono(VolDTO.class)
                .block();

        if (vol == null || vol.getPlacesDisponibles() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // 2. Récupération des détails de l'Hôtel et vérification de la disponibilité (via FeignClient)
        HotelDTO hotel = hotelRestClient.getHotelDetails(request.getHotelId());
        Boolean hotelDispo = hotelRestClient.verifierDispo(request.getHotelId(), request.getTypeChambre());

        if (hotel == null || !hotelDispo) {
            return ResponseEntity.badRequest().build();
        }

        // 3. Calcul du montant total (simplifié)
        // Note: Le prix de l'hôtel est simulé dans le DTO pour l'exemple
        BigDecimal montantTotal = vol.getPrix().add(hotel.getPrixParNuit());

        // 4. Création de la réservation en attente de paiement
        Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setClientEmail(request.getClientEmail());
        reservation.setStatutReservation(StatutReservation.EN_ATTENTE_PAIEMENT);
        reservation.setVolId(request.getVolId());
        reservation.setHotelId(request.getHotelId());
        reservation.setMontantTotal(montantTotal);
        reservation = reservationRepository.save(reservation);

        // 5. Paiement (via PaiementService)
        try {
            String transactionId = paiementService.payer(montantTotal);

            // 6. Confirmation et mise à jour
            reservation.setTransactionPaiementId(transactionId);
            reservation.setStatutReservation(StatutReservation.CONFIRMEE);

            // TODO: Ajouter ici la logique pour décrémenter les places dans vol-service et marquer la chambre comme non disponible dans hotel-service.

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
