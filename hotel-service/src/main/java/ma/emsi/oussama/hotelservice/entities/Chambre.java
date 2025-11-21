package ma.emsi.oussama.hotelservice.entities;

import ma.emsi.oussama.hotelservice.enums.TypeChambre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Chambre {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    @Enumerated(EnumType.STRING)
    private TypeChambre type;
    private BigDecimal prixParNuit;
    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
