package app.roomready.roomready.booking.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListEquipment {

    private String equipmentId;

    private Integer quantity;

}
