package app.roomready.roomready.booking.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_employee")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee extends DateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String division;

    private String position;

    @Column(name = "contact_info")
    private String contactInfo;

    @OneToOne(mappedBy = "employee")
    @JsonManagedReference
    private UserCredential userCredential;

    @OneToOne
    @JoinColumn(name = "reservation_id",referencedColumnName = "id")
    @JsonBackReference
    private Reservation reservation;
}
