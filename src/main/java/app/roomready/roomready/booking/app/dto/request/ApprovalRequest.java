package app.roomready.roomready.booking.app.dto.request;

import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.constant.ETrans;
import app.roomready.roomready.booking.app.entity.Approval;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

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

    private String reservationId;

    @Enumerated(EnumType.STRING)
//    @Column(length = 25)
    private ETrans acceptanceStatus;

    @Column(name = "approval_by")
    private String approvedBy;

    private String rejectionReason;


}
