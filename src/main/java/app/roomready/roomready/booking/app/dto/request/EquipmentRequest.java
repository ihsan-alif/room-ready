package app.roomready.roomready.booking.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentRequest {

    @JsonIgnore
    private String id;
    @NotEmpty(message = "Name has not be empty!")
    private String name;

    @NotNull(message = "Quantity has not be null!")
    private Integer quantity;
}
