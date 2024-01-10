package app.roomready.roomready.booking.app.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    @JsonIgnore
    private String id;

    private String employeeName;

    private String roomName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reservation;

    private Boolean status;

    private List<String> equipmentNeeds;
}
