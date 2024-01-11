package app.roomready.roomready.booking.app.entity;

import app.roomready.roomready.booking.app.constant.ERoom;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_room")
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Integer capacities;

    @Enumerated(EnumType.STRING)
    private ERoom status;

//    private Boolean status;

    private String facilities;
}
