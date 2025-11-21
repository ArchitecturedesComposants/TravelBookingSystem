package ma.emsi.oussama.hotelservice.controllers;

import ma.emsi.oussama.hotelservice.entities.Chambre;
import ma.emsi.oussama.hotelservice.entities.Hotel;
import ma.emsi.oussama.hotelservice.repositories.HotelRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HotelController {

    private final HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    // Simule la méthode getHotelDetails(Long id)
    @GetMapping("/hotels/{id}")
    public Hotel getHotelDetails(@PathVariable Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    // Simule la méthode verifierDispo(Long id, String type)
    @GetMapping("/hotels/{id}/disponibilite/{type}")
    public Boolean verifierDispo(@PathVariable Long id, @PathVariable String type) {
        // Logique de vérification de disponibilité simplifiée
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (hotel.isPresent()) {
            return hotel.get().getChambres().stream()
                    .anyMatch(chambre -> chambre.getType().name().equalsIgnoreCase(type) && chambre.getDisponible());
        }
        return false;
    }
}
