package app.roomready.roomready.booking.app.entity;

import app.roomready.roomready.booking.app.dto.response.RoomResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;
import java.util.List;

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

    private Boolean status = false;


    @OneToOne(mappedBy = "reservation")
    @JsonManagedReference
    private Employee employee;

    @OneToOne(mappedBy = "reservation")
    @JsonManagedReference
    private Room room;

    @OneToOne(mappedBy = "reservation")
    @JsonManagedReference
    private Approval approval;

    @OneToMany(mappedBy = "reservation")
    @JsonManagedReference
    private List<EquipmentNeeds> equipmentNeeds;

}
