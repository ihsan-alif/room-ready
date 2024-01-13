package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.ReservationGetAllRequest;
import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Reservation;
import app.roomready.roomready.booking.app.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request){
        ReservationResponse reservationResponse = reservationService.create(request);
        WebResponse<ReservationResponse> response = WebResponse.<ReservationResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Successfully create reservation")
                .data(reservationResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(defaultValue = "1") Integer page){

        ReservationRequest pageRequest = ReservationRequest.builder()
                .size(size)
                .page(page)
                .build();

        Page<Reservation> getAllPage = reservationService.getAll(pageRequest);
        PagingResponse pagingResponse = PagingResponse.builder()
                .page(pageRequest.getPage())
                .size(pageRequest.getSize())
                .totalPage(getAllPage.getTotalPages())
                .totalElements(getAllPage.getTotalElements())
                .build();

        WebResponse<?> webResponse = WebResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Success Get All Data")
                .paging(pagingResponse)
                .data(getAllPage.getContent())
                .build();
        return ResponseEntity.ok().body(webResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){

        ReservationResponse findbyId = reservationService.findById(id);
        return ResponseEntity.ok(findbyId);
    }

//    @PutMapping
//    public ResponseEntity<?> update(@RequestBody ReservationRequest request){
//        ReservationResponse update = reservationService.update(request);
//
//        return ResponseEntity.ok(update);
//    }

}
