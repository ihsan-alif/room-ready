package app.roomready.roomready.booking.app.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {

    private String id;

    private String name;

    private String division;

    private String position;

    private String contactInfo;

    private String username;
}
