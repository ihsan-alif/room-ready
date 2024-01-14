package app.roomready.roomready.booking.app.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListEquipmentNeeds {

    private String id;

    private String name;

    private Integer quantity;
}