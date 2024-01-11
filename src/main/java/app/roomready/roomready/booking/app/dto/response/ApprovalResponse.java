package app.roomready.roomready.booking.app.dto.response;

import app.roomready.roomready.booking.app.constant.ETrans;
import app.roomready.roomready.booking.app.entity.Approval;
import app.roomready.roomready.booking.app.entity.Reservation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private ETrans status;
    @Enumerated(EnumType.STRING)
    private ETrans acceptance;

}
