package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.entity.Reservation;
import app.roomready.roomready.booking.app.repository.ReservationRepository;
import app.roomready.roomready.booking.app.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;


    @Override
    public Reservation create(Reservation request) {
        return reservationRepository.save(request);
    }

    @Override
    public Reservation findById(String request) {
        Optional<Reservation> byId = reservationRepository.findById(request);

        return byId.orElseThrow();
    }
}
