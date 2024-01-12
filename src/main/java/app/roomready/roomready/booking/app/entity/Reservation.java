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
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "reservation_date")
    private Date reservationDate;

    @Enumerated(EnumType.STRING)
//    @Column(length = 25)
    private ETrans status;

    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "equipment_needs_id")
    private EquipmentNeeds equipmentNeeds;

}
