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
    @JsonIgnore
    private String id;

    @Column(name = "approval_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approval;

    private String employeeName;

//    @Column(length = 25)
    @Enumerated(EnumType.STRING)
    private ERoom statusRoom;
    @Enumerated(EnumType.STRING)
//    @Column(length = 25)
    private ETrans acceptanceStatus;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
//    @JsonBackReference
//    @JsonIgnore
//    private Reservation reservation;
}