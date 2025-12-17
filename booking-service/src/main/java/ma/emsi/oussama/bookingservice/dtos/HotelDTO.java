package ma.emsi.oussama.bookingservice.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelDTO {
    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    // Ajout d'un prix par nuit pour le calcul du montant total
    private BigDecimal prixParNuit = new BigDecimal("100.00");
}
