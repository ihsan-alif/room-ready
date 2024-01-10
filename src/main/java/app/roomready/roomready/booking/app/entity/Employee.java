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

<<<<<<< HEAD
    @OneToOne(mappedBy = "employee")
    @JsonManagedReference
=======
    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @OneToOne
    @JoinColumn(name = "user_id")
>>>>>>> 81de514c85b30fb2291f14d3e46032a175a8b1c5
    private UserCredential userCredential;

    @OneToOne
    @JoinColumn(name = "reservation_id",referencedColumnName = "id")
    @JsonBackReference
    private Reservation reservation;
}
