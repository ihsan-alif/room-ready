package app.roomready.roomready.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipment_needs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class EquipmentNeeds extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Long stock;
}
