package app.roomready.roomready.booking.app.entity;

import app.roomready.roomready.booking.app.constant.ETrans;
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
    private Date approvalDate;

    @Enumerated(EnumType.STRING)
    private ETrans acceptanceStatus;

    @Column(name = "approved_by")
    private String approvedBy;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}