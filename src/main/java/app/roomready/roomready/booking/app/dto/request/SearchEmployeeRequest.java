package app.roomready.roomready.booking.app.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchEmployeeRequest {

    private String name;

    private String division;

    private String position;

    private String contactInfo;

    private Integer page;

    private Integer size;
}
