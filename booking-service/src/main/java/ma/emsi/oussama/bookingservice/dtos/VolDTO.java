package ma.emsi.oussama.bookingservice.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VolDTO {
    private Long id;
    private String numeroVol;
    private String aeroportDepart;
    private String aeroportArrivee;
    private LocalDateTime dateHeureDepart;
    private BigDecimal prix;
    private Integer placesDisponibles;
}
