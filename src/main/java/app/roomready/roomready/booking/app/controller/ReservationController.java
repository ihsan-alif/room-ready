package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Reservation;
import app.roomready.roomready.booking.app.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<?> getAll(@RequestParam(required = false,defaultValue = "1") Integer size,
                                    @RequestParam(required = false,defaultValue = "10") Integer page){
        ReservationRequest PageRequest = ReservationRequest.builder()
                .size(size)
                .page(page)
                .build();
        Page<Reservation> getAllPage = reservationService.getAll(PageRequest);
        return ResponseEntity.ok().body(getAllPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){

        ReservationResponse findbyId = reservationService.findById(id);
        return ResponseEntity.ok(findbyId);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody ReservationRequest request){
        ReservationResponse update = reservationService.update(request);

        return ResponseEntity.ok(update);
    }

}
