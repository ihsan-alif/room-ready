package app.roomready.roomready.booking.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    @JsonIgnore
    private Integer size;
    @JsonIgnore
    private Integer page;

    @JsonIgnore
    private String id;

    private String employee;

    private String room;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reservation;

    private List<String> equipmentNeeds;
}
