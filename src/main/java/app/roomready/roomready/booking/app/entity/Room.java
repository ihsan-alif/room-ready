package app.roomready.roomready.booking.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Integer capacities;

    private Boolean status;

    private String facilities;

    @OneToOne
    @JoinColumn(name = "reservation_id",referencedColumnName = "id")
    @JsonBackReference
    @JsonIgnore
    private Reservation reservation;
}
