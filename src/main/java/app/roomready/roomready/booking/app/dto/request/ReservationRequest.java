package app.roomready.roomready.booking.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    @JsonIgnore
    private String id;

    @NotEmpty(message = "Employee not be empty!")
    private String employeeId;

    @NotEmpty(message = "Room not be empty!")
    private String roomId;

    @NotEmpty(message = "reservation date not be empty!")
    private String reservationDate;

    private List<ListEquipment> equipmentNeeds;
}
