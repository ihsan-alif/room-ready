package app.roomready.roomready.booking.app.dto.request;

import app.roomready.roomready.booking.app.entity.Employee;
import app.roomready.roomready.booking.app.entity.Reservation;
import app.roomready.roomready.booking.app.entity.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {


    @JsonIgnore
    private Integer size;

    @JsonIgnore
    private Integer page;

    private String id;

    private String employeeId;


    private String roomId;

    private String reservationDate;

    private List<ListEquipment> equipmentNeeds;
}
