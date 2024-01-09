package app.roomready.roomready.booking.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomUpdateRequest {
    @NotBlank(message = "id is required")
    private String id;
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "capacities is required")
    private Integer capacities;
    @NotBlank(message = "status is required")
    private Boolean status;
    private String facilities;
}
