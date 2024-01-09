package app.roomready.roomready.booking.app.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchEquipmentRequest {

    private String name;

    private Integer quantity;

    private Integer page;

    private Integer size;
}
