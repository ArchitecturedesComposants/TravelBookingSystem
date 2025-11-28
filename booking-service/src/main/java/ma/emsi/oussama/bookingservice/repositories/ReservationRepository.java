package ma.emsi.oussama.bookingservice.repositories;

import ma.emsi.oussama.bookingservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
