package ma.emsi.oussama.bookingservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ma.emsi.oussama.bookingservice.enums.StatutReservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateReservation;
    private String clientEmail;
    @Enumerated(EnumType.STRING)
    private StatutReservation statutReservation;
    private Long volId;
    private Long hotelId;
    private BigDecimal montantTotal;
    private String transactionPaiementId;

    // New fields for detailed booking information
    private String passengerName;
    private String typeChambre; // Room type: SIMPLE, DOUBLE, SUITE
    private String seatNumber; // Seat number for flights
    private String flightClass; // Flight class: ECONOMY, BUSINESS, FIRST
    private LocalDate checkInDate; // Hotel check-in date
    private LocalDate checkOutDate; // Hotel check-out date

    // User ownership
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // Expose userId for JSON serialization
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}
