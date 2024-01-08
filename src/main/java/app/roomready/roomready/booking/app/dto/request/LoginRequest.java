package app.roomready.roomready.booking.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "username must not be blank")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "invalid username")
    @Size(min = 6, max = 20, message = "username must be between 8 - 20 characters")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min = 8, message = "password must be greater than 8 characters")
    private String password;
}
