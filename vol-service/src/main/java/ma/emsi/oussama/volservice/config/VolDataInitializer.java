package ma.emsi.oussama.volservice.config;

import ma.emsi.oussama.volservice.entities.Vol;
import ma.emsi.oussama.volservice.repositories.VolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class VolDataInitializer {

    @Bean
    public CommandLineRunner initData(VolRepository volRepository) {
        return args -> {
            // Flight 1 - Paris to New York
            volRepository.save(new Vol(null, "AF001", "Paris CDG", "New York JFK",
                    LocalDateTime.now().plusDays(1).plusHours(8), LocalDateTime.now().plusDays(1).plusHours(16),
                    new BigDecimal("650.00"), 180));

            // Flight 2 - Tokyo to Seoul
            volRepository.save(new Vol(null, "JL102", "Tokyo Haneda", "Seoul Incheon",
                    LocalDateTime.now().plusDays(2).plusHours(10), LocalDateTime.now().plusDays(2).plusHours(13),
                    new BigDecimal("320.00"), 150));

            // Flight 3 - London to Singapore
            volRepository.save(new Vol(null, "SQ321", "London Heathrow", "Singapore Changi",
                    LocalDateTime.now().plusDays(3).plusHours(22), LocalDateTime.now().plusDays(4).plusHours(18),
                    new BigDecimal("850.00"), 200));

            // Flight 4 - Dubai to Paris
            volRepository.save(new Vol(null, "EK073", "Dubai International", "Paris CDG",
                    LocalDateTime.now().plusDays(1).plusHours(14), LocalDateTime.now().plusDays(1).plusHours(20),
                    new BigDecimal("480.00"), 220));

            // Flight 5 - Miami to Cancun
            volRepository.save(new Vol(null, "AA456", "Miami International", "Cancun",
                    LocalDateTime.now().plusDays(5).plusHours(9), LocalDateTime.now().plusDays(5).plusHours(11),
                    new BigDecimal("250.00"), 160));

            // Flight 6 - Amsterdam to Barcelona
            volRepository.save(new Vol(null, "KL1673", "Amsterdam Schiphol", "Barcelona El Prat",
                    LocalDateTime.now().plusDays(2).plusHours(7), LocalDateTime.now().plusDays(2).plusHours(9),
                    new BigDecimal("180.00"), 140));

            // Flight 7 - Sydney to Auckland
            volRepository.save(new Vol(null, "QF143", "Sydney Kingsford", "Auckland",
                    LocalDateTime.now().plusDays(4).plusHours(6), LocalDateTime.now().plusDays(4).plusHours(11),
                    new BigDecimal("380.00"), 175));

            // Flight 8 - Istanbul to Rome
            volRepository.save(new Vol(null, "TK1863", "Istanbul Airport", "Rome Fiumicino",
                    LocalDateTime.now().plusDays(3).plusHours(11), LocalDateTime.now().plusDays(3).plusHours(13),
                    new BigDecimal("220.00"), 165));

            // Flight 9 - Los Angeles to Honolulu
            volRepository.save(new Vol(null, "HA11", "Los Angeles LAX", "Honolulu",
                    LocalDateTime.now().plusDays(6).plusHours(8), LocalDateTime.now().plusDays(6).plusHours(13),
                    new BigDecimal("420.00"), 190));

            // Flight 10 - Frankfurt to Cape Town
            volRepository.save(new Vol(null, "LH572", "Frankfurt Airport", "Cape Town",
                    LocalDateTime.now().plusDays(7).plusHours(20), LocalDateTime.now().plusDays(8).plusHours(8),
                    new BigDecimal("780.00"), 210));
        };
    }
}
