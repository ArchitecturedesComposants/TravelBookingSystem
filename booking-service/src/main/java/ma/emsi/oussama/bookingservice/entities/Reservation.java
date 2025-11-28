package ma.emsi.oussama.bookingservice.entities;

import ma.emsi.oussama.bookingservice.enums.StatutReservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateReservation;
    private String clientEmail;
    @Enumerated(EnumType.STRING)
    private StatutReservation statutReservation;
    private Long volId;
    private Long hotelId;
    private BigDecimal montantTotal;
    private String transactionPaiementId;
}
