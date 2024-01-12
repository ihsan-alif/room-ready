package app.roomready.roomready.booking.app.dto.request;

import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.constant.ETrans;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalRequestReservation {
    @NotBlank(message = "id reservation is required")
    private String reservationId;
    @NotNull(message = "status is required")
    @Pattern(regexp = "^(DESCLINE|ACCEPT)$", message = "status must be 'accept' or 'descline'")
    @Enumerated(EnumType.STRING)
    private ETrans approvedStatus;
    @NotBlank(message = "name approved must not blank")
    private String approvedBy;

    private String rejection;

//    private String idRoom;




}
