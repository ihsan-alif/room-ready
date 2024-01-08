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

    private Approval approval;

    private Reservation reservation;
}
