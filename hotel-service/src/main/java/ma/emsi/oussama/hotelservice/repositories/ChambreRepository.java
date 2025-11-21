package ma.emsi.oussama.hotelservice.repositories;

import ma.emsi.oussama.hotelservice.entities.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
}
