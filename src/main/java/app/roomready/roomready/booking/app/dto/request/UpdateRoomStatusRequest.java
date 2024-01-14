package app.roomready.roomready.booking.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoomStatusRequest {
    @JsonIgnore
    @NotBlank(message = "id is required")
    private String id;

    @NotNull(message = "status is required")
    @Pattern(regexp = "^(available|booked)$", message = "status must be 'available' or 'booked'")
    private String status;
}
