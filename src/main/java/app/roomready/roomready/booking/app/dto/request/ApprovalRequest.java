package app.roomready.roomready.booking.app.dto.request;

import app.roomready.roomready.booking.app.entity.Approval;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalRequest {

    private Integer size;

    private Integer page;

    private String name;

    private Boolean status;


}
