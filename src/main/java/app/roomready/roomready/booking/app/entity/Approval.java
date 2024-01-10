package app.roomready.roomready.booking.app.entity;

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

    @Column(name = "approval_status")
    private Boolean status;

    private String rejection;

    @OneToOne
    @JoinColumn(name = "reservation_id",referencedColumnName = "id")
    @JsonBackReference
    @JsonIgnore
    private Reservation reservation;
//    @OneToOne
//    @JoinColumn(name = "room_id",referencedColumnName = "id")
//    @JsonBackReference
//    @JsonIgnore
//    private Room room;
}
