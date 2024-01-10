package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.ReservationGetAllRequest;
import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationService {

    ReservationResponse create (ReservationRequest request);
    ReservationResponse createReservation (Reservation request);
    ReservationResponse findById (String request);

    Page<Reservation> getAll (ReservationGetAllRequest request);

    void deleteById(String request);

//    ReservationResponse update(ReservationRequest request);



}
