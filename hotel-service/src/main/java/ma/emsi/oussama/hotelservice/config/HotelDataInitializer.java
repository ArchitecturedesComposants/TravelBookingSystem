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
            // Hotel 1 - Mountain Lodge
            Hotel hotel1 = new Hotel(null, "Alpine Mountain Lodge", "123 Mountain View Road", "Chamonix", null);
            hotelRepository.save(hotel1);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "101", TypeChambre.SIMPLE, new BigDecimal("120.00"), true, hotel1),
                    new Chambre(null, "102", TypeChambre.DOUBLE, new BigDecimal("200.00"), true, hotel1),
                    new Chambre(null, "201", TypeChambre.SUITE, new BigDecimal("450.00"), true, hotel1)));

            // Hotel 2 - City Hotel
            Hotel hotel2 = new Hotel(null, "Manhattan Grand Hotel", "789 Fifth Avenue", "New York", null);
            hotelRepository.save(hotel2);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "301", TypeChambre.DOUBLE, new BigDecimal("350.00"), true, hotel2),
                    new Chambre(null, "302", TypeChambre.SIMPLE, new BigDecimal("220.00"), true, hotel2),
                    new Chambre(null, "401", TypeChambre.SUITE, new BigDecimal("650.00"), true, hotel2)));

            // Hotel 3 - Coastal Resort
            Hotel hotel3 = new Hotel(null, "Côte d'Azur Beach Resort", "15 Promenade des Anglais", "Nice", null);
            hotelRepository.save(hotel3);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "101", TypeChambre.DOUBLE, new BigDecimal("280.00"), true, hotel3),
                    new Chambre(null, "102", TypeChambre.SUITE, new BigDecimal("520.00"), true, hotel3)));

            // Hotel 4 - Luxury Classic
            Hotel hotel4 = new Hotel(null, "Ritz Paris", "15 Place Vendôme", "Paris", null);
            hotelRepository.save(hotel4);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "501", TypeChambre.SUITE, new BigDecimal("890.00"), true, hotel4),
                    new Chambre(null, "502", TypeChambre.DOUBLE, new BigDecimal("450.00"), true, hotel4)));

            // Hotel 5 - Jungle Resort
            Hotel hotel5 = new Hotel(null, "Amazon Rainforest Lodge", "Rio Negro Basin", "Manaus", null);
            hotelRepository.save(hotel5);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "A1", TypeChambre.SIMPLE, new BigDecimal("180.00"), true, hotel5),
                    new Chambre(null, "A2", TypeChambre.DOUBLE, new BigDecimal("320.00"), true, hotel5)));

            // Hotel 6 - Desert Villa
            Hotel hotel6 = new Hotel(null, "Sahara Desert Oasis", "Route du Désert", "Marrakech", null);
            hotelRepository.save(hotel6);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "V1", TypeChambre.SUITE, new BigDecimal("380.00"), true, hotel6),
                    new Chambre(null, "V2", TypeChambre.DOUBLE, new BigDecimal("240.00"), true, hotel6)));

            // Hotel 7 - Island Paradise
            Hotel hotel7 = new Hotel(null, "Maldives Water Villa", "Malé Atoll", "Maldives", null);
            hotelRepository.save(hotel7);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "W1", TypeChambre.SUITE, new BigDecimal("950.00"), true, hotel7),
                    new Chambre(null, "W2", TypeChambre.DOUBLE, new BigDecimal("650.00"), true, hotel7)));

            // Hotel 8 - Historic Castle
            Hotel hotel8 = new Hotel(null, "Edinburgh Castle Hotel", "Royal Mile", "Edinburgh", null);
            hotelRepository.save(hotel8);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "T1", TypeChambre.DOUBLE, new BigDecimal("290.00"), true, hotel8),
                    new Chambre(null, "T2", TypeChambre.SUITE, new BigDecimal("480.00"), true, hotel8)));

            // Hotel 9 - Modern Spa
            Hotel hotel9 = new Hotel(null, "Kyoto Zen Spa Resort", "Higashiyama District", "Kyoto", null);
            hotelRepository.save(hotel9);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "Z1", TypeChambre.SIMPLE, new BigDecimal("200.00"), true, hotel9),
                    new Chambre(null, "Z2", TypeChambre.SUITE, new BigDecimal("520.00"), true, hotel9)));

            // Hotel 10 - Ski Resort
            Hotel hotel10 = new Hotel(null, "Swiss Alps Chalet", "Bahnhofstrasse", "Zermatt", null);
            hotelRepository.save(hotel10);
            chambreRepository.saveAll(Arrays.asList(
                    new Chambre(null, "C1", TypeChambre.DOUBLE, new BigDecimal("340.00"), true, hotel10),
                    new Chambre(null, "C2", TypeChambre.SUITE, new BigDecimal("580.00"), true, hotel10)));
        };
    }
}
