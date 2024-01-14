package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,String>, JpaSpecificationExecutor<Reservation> {

    List<Reservation> findByEmployeeId(String employeeId);
}
