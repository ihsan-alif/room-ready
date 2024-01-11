package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,String> {


}
