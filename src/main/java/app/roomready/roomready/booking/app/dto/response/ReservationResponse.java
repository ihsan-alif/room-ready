package app.roomready.roomready.booking.app.dto.response;


import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.constant.ETrans;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private ETrans status;

    private List<EquipmentNeedsResponse> equipmentNeeds;
}
