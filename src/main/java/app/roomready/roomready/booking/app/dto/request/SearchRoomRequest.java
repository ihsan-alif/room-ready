package app.roomready.roomready.booking.app.dto.request;

import app.roomready.roomready.booking.app.constant.ERoom;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRoomRequest {
    private Integer page;
    private Integer size;
    private String name;
    private String status;
}
