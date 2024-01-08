package app.roomready.roomready.booking.app.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResonse {
    private String username;
    private List<String> role;
}
