package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.entity.Reservation;
import org.springframework.data.domain.Page;

public interface ReservationService {

    ReservationResponse create (ReservationRequest request);
    ReservationResponse findById (String request);

    Page<Reservation> getAll (ReservationRequest request);

    ReservationResponse update(ReservationRequest request);



}
