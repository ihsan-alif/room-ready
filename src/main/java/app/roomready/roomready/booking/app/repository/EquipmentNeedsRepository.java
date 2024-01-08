package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.entity.EquipmentNeeds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentNeedsRepository extends JpaRepository<EquipmentNeeds, String> {

    Optional<EquipmentNeeds> findByName(String name);
}
