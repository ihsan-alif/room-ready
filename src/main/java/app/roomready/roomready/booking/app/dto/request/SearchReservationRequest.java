package app.roomready.roomready.booking.app.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchReservationRequest {

    private String startDate;

    private String endDate;

    private Integer page;

    private Integer size;

}
