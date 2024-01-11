package app.roomready.roomready.booking.app.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentNeedsResponse {

    private String id;

    private String name;

    private Long stock;
}
