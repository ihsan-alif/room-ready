package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.request.SearchReservationRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateStatusReservation;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import org.springframework.data.domain.Page;

public interface ReservationService {

    ReservationResponse create (ReservationRequest request);

    ReservationResponse findById (String request);

    ReservationResponse update(ReservationRequest request);

    ReservationResponse updateStatus(UpdateStatusReservation reservation);

    Page<ReservationResponse> search(SearchReservationRequest request);

    ReservationResponse getReservationByEmployeeId(String employeeId);
}
