package app.roomready.roomready.booking.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusReservation {

    @JsonIgnore
    private String id;

    @NotEmpty(message = "Status not be empty!")
    private String status;

    private String rejectionReason;
}