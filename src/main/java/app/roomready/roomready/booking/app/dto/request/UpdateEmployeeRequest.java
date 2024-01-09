package app.roomready.roomready.booking.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
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
}
