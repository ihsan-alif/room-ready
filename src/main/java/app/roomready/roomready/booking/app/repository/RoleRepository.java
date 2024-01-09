package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.constant.ERole;
import app.roomready.roomready.booking.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(ERole role);
}
