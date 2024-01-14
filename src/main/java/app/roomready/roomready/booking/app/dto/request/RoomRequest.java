package app.roomready.roomready.booking.app.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "capacities is required")
    @Min(value = 1, message = "value must be greater than or equal to 1")
    private Integer capacities;

    @NotNull(message = "status is required")
    @Pattern(regexp = "^(available|booked)$", message = "status must be 'available' or 'booked'")
    private String status;

    private String facilities;
}
