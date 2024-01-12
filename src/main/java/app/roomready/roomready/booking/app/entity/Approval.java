package app.roomready.roomready.booking.app.entity;

import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.constant.ETrans;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "approval_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approval;
    private String employeeName;

    @Enumerated(EnumType.STRING)
    private ERoom statusRoom;
    @Enumerated(EnumType.STRING)
    private ETrans acceptanceStatus;

    @Column(name = "rejection_reason")
    private String rejectionReason;
    @Column(name = "approved_by")
    private String approvedBy;

}