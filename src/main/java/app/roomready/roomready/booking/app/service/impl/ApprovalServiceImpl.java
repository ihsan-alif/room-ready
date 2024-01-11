package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.constant.ETrans;
import app.roomready.roomready.booking.app.dto.request.*;
import app.roomready.roomready.booking.app.dto.response.*;
import app.roomready.roomready.booking.app.entity.*;
import app.roomready.roomready.booking.app.exception.ErrorController;
import app.roomready.roomready.booking.app.repository.ApprovalRepository;
import app.roomready.roomready.booking.app.repository.ReservationRepository;
import app.roomready.roomready.booking.app.service.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;

    private final RoomService roomService;
    private final EmployeeService employeeService;
    private final ReservationRepository reservationRepository;
    private final EquipmentNeedsService equipmentNeedsService;

    private final UserService userService;
    @Override
    @Transactional(readOnly = true)
    public Page<Approval> getAll(ApprovalRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(
                (request.getPage() - 1), request.getSize());
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
//        Specification<Approval> approvalSpecification = getApprovalSpecification(request);
        return approvalRepository.findAll(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Approval getById(String request) {
        return approvalRepository.findById(request).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Approval Not Found")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  create(Approval request) {
        approvalRepository.save(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String request) {
        Optional<Approval> byIdResult = approvalRepository.findById(request);
        if (byIdResult.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not Found");
        approvalRepository.deleteById(request);
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(ApprovalRequestReservation request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId()).orElseThrow();

        RoomResponse roomResponse = roomService.getById(request.getIdRoom());

        Approval approval = approvalRepository.findById(request.getApprovedId()).orElseThrow();
        Room room = Room.builder()
                .name(roomResponse.getName())
                .facilities(roomResponse.getFacilities())
                .status(ERoom.AVAILABLE)
                .capacities(roomResponse.getCapacities())
                .id(request.getIdRoom())
                .build();

        Reservation reservation1 = Reservation.builder()
                .status(request.getApprovedStatus())
                .room(reservation.getRoom())
                .reservationDate(reservation.getReservationDate())
                .employee(reservation.getEmployee())
                .quantity(reservation.getQuantity())
                .id(reservation.getId())
                .build();
        reservationRepository.saveAndFlush(reservation1);

        Approval approvalSave = Approval.builder()
                .approval(approval.getApproval())
                .employeeName(approval.getEmployeeName())
                .acceptanceStatus(request.getApprovedStatus())
                .statusRoom(room.getStatus())
                .build();
        approvalRepository.saveAndFlush(approvalSave);

        if (reservation.getStatus().equals(ETrans.ACCEPT)){
            room.setStatus(ERoom.BOOKED);
        }

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.builder()
                .capacities(room.getCapacities())
                .status(room.getStatus().toString().toLowerCase())
                .facilities(room.getFacilities())
                .name(room.getName())
                .id(room.getId())
                .build();
        roomService.update(roomUpdateRequest);


    }


}
