package ma.emsi.oussama.bookingservice.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {
    private Long volId;
    private Long hotelId;
    private String typeChambre;
    private String clientEmail;

    // New fields for detailed booking
    private String passengerName;
    private String seatNumber;
    private String flightClass;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
