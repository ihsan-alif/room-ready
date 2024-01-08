package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.entity.Reservation;

public interface ReservationService {

    Reservation create (Reservation request);
    Reservation findById (String request);


}
