package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval,String>, JpaSpecificationExecutor<Approval> {
}
