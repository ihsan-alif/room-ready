package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
