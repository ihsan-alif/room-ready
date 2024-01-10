package app.roomready.roomready.booking.app.dto.request;

import app.roomready.roomready.booking.app.entity.Approval;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalRequest {

    @JsonIgnore
    private Integer size;
    @JsonIgnore
    private Integer page;

    private String id;

    @NotEmpty(message = "Name has not be empty!")
    private String idName;

    private Boolean statusRoom;

    private Boolean statusAccceptance;


}
