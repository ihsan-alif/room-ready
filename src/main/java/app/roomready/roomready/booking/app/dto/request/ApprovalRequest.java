package app.roomready.roomready.booking.app.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalRequest {

    private Integer size;

    private Integer page;


}
