package app.roomready.roomready.booking.app.dto.response;

import app.roomready.roomready.booking.app.entity.Approval;
import app.roomready.roomready.booking.app.entity.Reservation;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalResponse {

    private String date;
    private String id;
    private String name;
    private Boolean status;
    private String acceptance;

}
