package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.entity.Reservation;
import app.roomready.roomready.booking.app.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return ResponseEntity.ok(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false,defaultValue = "1") Integer size,
                                    @RequestParam(required = false,defaultValue = "1") Integer page){
        ReservationRequest PageRequest = ReservationRequest.builder()
                .size(size)
                .page(page)
                .build();
        Page<Reservation> getAllPage = reservationService.getAll(PageRequest);
        return ResponseEntity.ok().body(getAllPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){

        ReservationResponse FindbyId = reservationService.findById(id);
        return ResponseEntity.ok(FindbyId);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody ReservationRequest request){
        ReservationResponse update = reservationService.update(request);

        return ResponseEntity.ok(update);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        reservationService.deleteById(id);

        return  ResponseEntity.ok("Succed Delete By Id");
    }
}
