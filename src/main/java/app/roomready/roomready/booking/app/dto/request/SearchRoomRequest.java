package app.roomready.roomready.booking.app.dto.request;

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
    private Boolean status;
}
