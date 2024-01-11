package app.roomready.roomready.booking.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotNull(message = "capacities is required")
    @Min(value = 1, message = "capacities must be greater than or equal to 1")
    private Integer capacities;
    @NotNull(message = "status is required")
    private Boolean status;
    private String facilities;
}
