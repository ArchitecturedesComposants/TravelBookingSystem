package ma.emsi.oussama.hotelservice.config;

import ma.emsi.oussama.hotelservice.entities.Chambre;
import ma.emsi.oussama.hotelservice.entities.Hotel;
import ma.emsi.oussama.hotelservice.enums.TypeChambre;
import ma.emsi.oussama.hotelservice.repositories.ChambreRepository;
import ma.emsi.oussama.hotelservice.repositories.HotelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class HotelDataInitializer {

    @Bean
    public CommandLineRunner initData(HotelRepository hotelRepository, ChambreRepository chambreRepository) {
        return args -> {
            // Création de l'hôtel 1
            Hotel hotel1 = new Hotel(null, "The Plaza", "5th Avenue", "New York", null);
            hotelRepository.save(hotel1);

            Chambre ch1_1 = new Chambre(null, "101", TypeChambre.SIMPLE, new BigDecimal("150.00"), true, hotel1);
            Chambre ch1_2 = new Chambre(null, "102", TypeChambre.DOUBLE, new BigDecimal("250.00"), true, hotel1);
            Chambre ch1_3 = new Chambre(null, "201", TypeChambre.SUITE, new BigDecimal("500.00"), false, hotel1);

            chambreRepository.saveAll(Arrays.asList(ch1_1, ch1_2, ch1_3));

            // Création de l'hôtel 2
            Hotel hotel2 = new Hotel(null, "Ritz Paris", "Place Vendôme", "Paris", null);
            hotelRepository.save(hotel2);

            Chambre ch2_1 = new Chambre(null, "301", TypeChambre.DOUBLE, new BigDecimal("300.00"), true, hotel2);
            Chambre ch2_2 = new Chambre(null, "302", TypeChambre.SIMPLE, new BigDecimal("180.00"), true, hotel2);

            chambreRepository.saveAll(Arrays.asList(ch2_1, ch2_2));
        };
    }
}
