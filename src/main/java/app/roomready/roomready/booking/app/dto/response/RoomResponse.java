package app.roomready.roomready.booking.app.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {
    private String id;
    private String name;
    private Integer capacities;
    private String status;
    private String facilities;
}
