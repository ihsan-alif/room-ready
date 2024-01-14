package app.roomready.roomready.booking.app.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private String id;

    private String employeeName;

    private String roomName;

    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    private Date reservationDate;

    private String status;

    private List<ListEquipmentNeeds> equipmentNeeds;
}
