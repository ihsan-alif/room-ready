package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.entity.EquipmentNeeds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EquipmentNeedsRepository extends JpaRepository<EquipmentNeeds, String>, JpaSpecificationExecutor<EquipmentNeeds> {

    Optional<EquipmentNeeds> findByName(String name);

    List<EquipmentNeeds> findAllById(String id);
}
