package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.constant.ETrans;
import app.roomready.roomready.booking.app.dto.request.ListEquipment;
import app.roomready.roomready.booking.app.dto.request.ReservationGetAllRequest;
import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.entity.*;
import app.roomready.roomready.booking.app.repository.EquipmentNeedsRepository;
import app.roomready.roomready.booking.app.repository.ReservationRepository;
import app.roomready.roomready.booking.app.service.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final RoomService roomService;

    private final EmployeeService employeeService;

    private final EquipmentNeedsService equipmentNeedsService;

    private final EquipmentNeedsRepository equipmentNeedsRepository;

    @SneakyThrows
    @Override
    public ReservationResponse create(ReservationRequest request) {
        Room roomById = roomService.get(request.getRoomId());
        Employee employee = employeeService.get(request.getEmployeeId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

        Date date = dateFormat.parse(request.getReservationDate());

        Reservation reservation = Reservation.builder()
                .room(roomById)
                .status(ETrans.PENDING)
                .reservationDate(date)
                .employee(employee)
                .build();

        List<EquipmentNeedsResponse> equipments = new ArrayList<>();

        for (ListEquipment equipment : request.getEquipmentNeeds()){
            EquipmentNeeds equipmentNeeds = equipmentNeedsService.get(equipment.getEquipmentId());

            reservation.setEquipmentNeeds(equipmentNeeds);
            reservation.setQuantity(equipment.getQuantity());

            if (equipmentNeeds.getStock() - reservation.getQuantity() < 0){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity exceeds Stock");
            }

            equipmentNeeds.setStock(equipmentNeeds.getStock() - reservation.getQuantity());
            equipmentNeedsService.update(equipmentNeeds);

            reservationRepository.save(reservation);

            EquipmentNeedsResponse response = EquipmentNeedsResponse.builder()
                    .id(reservation.getEquipmentNeeds().getId())
                    .name(reservation.getEquipmentNeeds().getName())
                    .stock(reservation.getEquipmentNeeds().getStock())
                    .build();

            equipments.add(response);
        }

        return  ReservationResponse.builder()
                .id(reservation.getId())
                .employeeName(reservation.getEmployee().getName())
                .roomName(reservation.getRoom().getName())
                .reservationDate(date)
                .status(reservation.getStatus().getDisplayValue())
                .equipmentNeeds(equipments)
                .build();
    }

    public ReservationResponse findById(String id) {
        Optional<Reservation> byId = reservationRepository.findById(id);
        byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
        return ReservationResponse.builder()
                .reservationDate(byId.get().getReservationDate())
                .employeeName(byId.get().getEmployee().getName())
                .roomName(byId.get().getRoom().getName())
                .build();
    }

    @Override
    public Page<Reservation> getAll(ReservationGetAllRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(
                (request.getPage() - 1), request.getSize()
        );

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return reservationRepository.findAll(pageRequest);
    }



    @Override
    public ReservationResponse update(ReservationRequest request) {
        Optional<Reservation> byIdFind = reservationRepository.findById(request.getId());
        if (byIdFind.isEmpty()) throw new RuntimeException("Can not find Data");
        Reservation reservationSave = reservationRepository.save(byIdFind.get());
        return ReservationResponse.builder()
                .employeeName(reservationSave.getEmployee().getName())
                .reservationDate(reservationSave.getReservationDate())
                .roomName(reservationSave.getRoom().getName())
                .id(reservationSave.getId())
                .build();
    }
}
