package ma.emsi.oussama.hotelservice.repositories;

import ma.emsi.oussama.hotelservice.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
