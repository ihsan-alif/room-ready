package app.roomready.roomready.booking.app.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {


    private Integer page;

    private Integer size;

    private Integer totalPage;

    private Long totalElements;
}
