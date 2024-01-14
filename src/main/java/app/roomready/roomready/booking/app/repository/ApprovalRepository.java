package app.roomready.roomready.booking.app.repository;

import app.roomready.roomready.booking.app.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval,String>, JpaSpecificationExecutor<Approval> {

    @Query(value = """
            select a.id, a.approval_date, approved_by, a.acceptance_status, a.reservation_id, me.name, mr.name, r.reservation_date , r.status,
            r.equipment_needs_id , en.name, r.quantity
            from approval a
            join reservation r on(r.id = a.reservation_id)
            join m_employee me on(me.id = r.employee_id)
            join m_room mr on(mr.id = r.room_id)
            LEFT JOIN equipment_needs en ON en.id = r.equipment_needs_id;
            """, nativeQuery = true)
    List<Approval> download();
}
