package app.roomready.roomready.booking.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEmployeeRequest {

    @JsonIgnore
    private String id;

    @NotEmpty(message = "Name has not be empty!")
    private String name;

    @NotEmpty(message = "Division has not be empty!")
    private String division;

    @NotEmpty(message = "Position has not be empty!")
    private String position;

    @NotEmpty(message = "Contact Info has not be empty!")
    private String contactInfo;

    @NotBlank(message = "username must not be blank")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "invalid username")
    @Size(min = 6, max = 20, message = "username must be between 8 - 20 characters")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min = 8, message = "password must be greater than 8 characters")
    private String password;
}
