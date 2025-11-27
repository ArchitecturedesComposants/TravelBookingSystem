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
            volRepository.save(new Vol(null, "AF123", "Paris", "New York", LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(5), new BigDecimal("500.00"), 150));
            volRepository.save(new Vol(null, "LH456", "Francfort", "Tokyo", LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(10), new BigDecimal("850.50"), 80));
            volRepository.save(new Vol(null, "BA789", "Paris", "Londres", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(1), new BigDecimal("120.00"), 200));
            volRepository.save(new Vol(null, "EK001", "Dubai", "Paris", LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(3), new BigDecimal("450.00"), 100));
        };
    }
}
