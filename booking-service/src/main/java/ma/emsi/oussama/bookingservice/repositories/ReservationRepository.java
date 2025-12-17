package ma.emsi.oussama.bookingservice.repositories;

import ma.emsi.oussama.bookingservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientEmail(@Param("clientEmail") String clientEmail);
}
