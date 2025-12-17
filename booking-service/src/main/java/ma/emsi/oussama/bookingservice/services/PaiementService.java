package ma.emsi.oussama.bookingservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
public class PaiementService {

    // WebClient est injecté via le constructeur dans BookingController
    // Ici, on simule la logique de paiement
    public String payer(BigDecimal montant) {
        // Logique de paiement simulée.
        // En réalité, on ferait un appel WebClient vers un service de paiement externe.
        if (montant.compareTo(BigDecimal.ZERO) > 0) {
            // Simulation d'une transaction réussie
            return "TXN-" + System.currentTimeMillis();
        }
        throw new RuntimeException("Paiement échoué");
    }
}
