package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.request.SearchReservationRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateStatusReservation;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.ReservationService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<WebResponse<ReservationResponse>> createReservation(@RequestBody ReservationRequest request){
        ReservationResponse reservationResponse = reservationService.create(request);
        WebResponse<ReservationResponse> response = WebResponse.<ReservationResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Successfully create reservation")
                .data(reservationResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GA')")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(name = "startDate", required = false) String startDate,
                                    @RequestParam(name = "endDate", required = false) String endDate){

        SearchReservationRequest pageRequest = SearchReservationRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .size(size)
                .page(page)
                .build();

        Page<ReservationResponse> search = reservationService.search(pageRequest);
        PagingResponse pagingResponse = PagingResponse.builder()
                .page(pageRequest.getPage())
                .size(pageRequest.getSize())
                .totalPage(search.getTotalPages())
                .totalElements(search.getTotalElements())
                .build();

        WebResponse<?> webResponse = WebResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Success Get All Data")
                .paging(pagingResponse)
                .data(search.getContent())
                .build();
        return ResponseEntity.ok().body(webResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){

        ReservationResponse findbyId = reservationService.findById(id);
        WebResponse<ReservationResponse> response = WebResponse.<ReservationResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get reservation by id")
                .data(findbyId)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{employeeId}/employee")
    public ResponseEntity<WebResponse<List<ReservationResponse>>> getByEmployeeId(@PathVariable String employeeId){

        List<ReservationResponse> reservationByEmployeeId = reservationService.getReservationByEmployeeId(employeeId);
        WebResponse<List<ReservationResponse>> response = WebResponse.<List<ReservationResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get reservation by employee id")
                .data(reservationByEmployeeId)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<ReservationResponse>> update(@PathVariable String id,
                                    @RequestBody ReservationRequest request){
        request.setId(id);
        ReservationResponse update = reservationService.update(request);

        WebResponse<ReservationResponse> response = WebResponse.<ReservationResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update reservation")
                .data(update)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'GA')")
    public ResponseEntity<WebResponse<ReservationResponse>> updateStatus(@PathVariable String id,
                                          @RequestBody UpdateStatusReservation request){
        request.setId(id);
        ReservationResponse update = reservationService.updateStatus(request);

        WebResponse<ReservationResponse> response = WebResponse.<ReservationResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update status reservation")
                .data(update)
                .build();

        return ResponseEntity.ok(response);
    }

}
