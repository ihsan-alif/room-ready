package app.roomready.roomready.booking.app.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationGetAllRequest {

    private Integer size;
    private Integer page;
}
