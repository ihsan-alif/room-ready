package app.roomready.roomready.booking.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    @NotBlank(message = "name is required")
    private String name;
    private Integer capacities;
    private Boolean status;
    private String facilities;
}
