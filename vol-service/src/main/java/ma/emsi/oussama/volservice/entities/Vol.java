package ma.emsi.oussama.volservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Vol {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroVol;
    private String aeroportDepart;
    private String aeroportArrivee;
    private LocalDateTime heureDepart;
    private LocalDateTime dateHeureDepart;
    private BigDecimal prix;
    private Integer placesDisponibles;
}
