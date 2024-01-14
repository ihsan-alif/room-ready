package app.roomready.roomready.booking.app.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalResponse {

    private String id;

    private Date approvalDate;

    private String approvalBy;

    private String statusApproval;


    private List<ReservationResponse> responses;

}
