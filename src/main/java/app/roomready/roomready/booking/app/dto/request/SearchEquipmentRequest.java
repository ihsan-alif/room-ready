package app.roomready.roomready.booking.app.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchEquipmentRequest {

    private String name;

    private Long stock;

    private Integer page;

    private Integer size;
}
