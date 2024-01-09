package app.roomready.roomready.booking.app.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    private String name;
    private Integer capacities;
    private Boolean status;
    private String facilities;
}
