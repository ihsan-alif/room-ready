package app.roomready.roomready.booking.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEquipmentRequest {

    @NotEmpty(message = "Name has not be empty!")
    private String name;

    @NotBlank(message = "Quantity has not be blank!")
    private Integer quantity;
}
