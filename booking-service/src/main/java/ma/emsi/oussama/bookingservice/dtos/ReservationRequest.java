package ma.emsi.oussama.bookingservice.dtos;

import lombok.Data;

@Data
public class ReservationRequest {
    private Long volId;
    private Long hotelId;
    private String typeChambre;
    private String clientEmail;
}
