package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponse;
import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.entity.*;
import app.roomready.roomready.booking.app.repository.EquipmentNeedsRepository;
import app.roomready.roomready.booking.app.repository.ReservationRepository;
import app.roomready.roomready.booking.app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final EmployeeService employeeService;
    private final ApprovalService approvalService;

    private final EquipmentNeedsRepository equipmentNeedsRepository;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        Room roomById = roomService.getById(request.getRoom());
        Employee employee = employeeService.get(request.getEmployee());

        Reservation reservation = Reservation.builder()
                .room(roomById)
                .status(roomById.getStatus())
                .reservationDate(roomById.getReservation().getReservationDate())
                .employee(employee)
                .build();

        Reservation reservationSave = reservationRepository.save(reservation);

        return ReservationResponse.builder()
                .employeeName(reservationSave.getEmployee().getName())
                .reservation(reservationSave.getReservationDate())
                .roomName(reservationSave.getRoom().getName())
                .id(reservationSave.getId())
                .equipmentNeeds(getEquipmentNames())
                .build();
    }

    @Override
    public ReservationResponse findById(String request) {
        Optional<Reservation> byId = reservationRepository.findById(request);
        if (byId.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Not Found");
        return ReservationResponse.builder()
                .reservation(byId.get().getReservationDate())
                .employeeName(byId.get().getEmployee().getName())
                .status(byId.get().getStatus())
                .roomName(byId.get().getRoom().getName())
                .equipmentNeeds(getEquipmentNames())
                .build();
    }

    @Override
    public Page<Reservation> getAll(ReservationRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(
                (request.getPage() - 1), request.getSize()
        );

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return reservationRepository.findAll(pageRequest);
    }

    @Override
    public void deleteById(String request) {
        Optional<Reservation> byIdResult = reservationRepository.findById(request);
        if (byIdResult.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not Found");
        reservationRepository.deleteById(request);
    }

    @Override
    public ReservationResponse update(ReservationRequest request) {
        Optional<Reservation> byIdFind = reservationRepository.findById(request.getId());
        if (byIdFind.isEmpty()) throw new RuntimeException("Can not find Data");
        Reservation reservationSave = reservationRepository.save(byIdFind.get());
        return ReservationResponse.builder()
                .employeeName(reservationSave.getEmployee().getName())
                .reservation(reservationSave.getReservationDate())
                .roomName(reservationSave.getRoom().getName())
                .id(reservationSave.getId())
                .equipmentNeeds(getEquipmentNames())
                .build();
    }


    public List<String> getEquipmentNames() {
        List<EquipmentNeeds> equipmentNeedsList = equipmentNeedsRepository.findAll();
        return equipmentNeedsList.stream()
                .map(EquipmentNeeds::getName)
                .collect(Collectors.toList());
    }
}
