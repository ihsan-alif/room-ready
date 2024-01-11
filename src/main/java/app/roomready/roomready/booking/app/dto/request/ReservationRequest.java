package app.roomready.roomready.booking.app.dto.request;

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

    @JsonIgnore
    private String id;

    private String employeeId;

    private String roomId;

    private String reservationDate;

    private List<ListEquipment> equipmentNeeds;
}
