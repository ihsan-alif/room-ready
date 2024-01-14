package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.constant.ETrans;
import app.roomready.roomready.booking.app.dto.request.ListEquipment;
import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.request.SearchReservationRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateStatusReservation;
import app.roomready.roomready.booking.app.dto.response.ListEquipmentNeeds;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.entity.*;
import app.roomready.roomready.booking.app.repository.ReservationRepository;
import app.roomready.roomready.booking.app.service.*;
import app.roomready.roomready.booking.app.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final RoomService roomService;

    private final EmployeeService employeeService;

    private final EquipmentNeedsService equipmentNeedsService;

    private final ApprovalService approvalService;

    private final ValidationUtils utils;


    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationResponse create(ReservationRequest request) {

        utils.validate(request);

        Room roomById = roomService.get(request.getRoomId());

        if (roomById.getStatus().equals(ERoom.BOOKED)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room no Available");

        Employee employee = employeeService.get(request.getEmployeeId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

        Date date = dateFormat.parse(request.getReservationDate());

        Reservation reservation = Reservation.builder()
                .room(roomById)
                .status(ETrans.PENDING)
                .reservationDate(date)
                .employee(employee)
                .build();

        List<ListEquipmentNeeds> equipments = new ArrayList<>();

        List<ListEquipment> equipmentNeedsList = request.getEquipmentNeeds();

        if (equipmentNeedsList != null && !equipmentNeedsList.isEmpty()) {
            for (ListEquipment equipment : equipmentNeedsList) {
                EquipmentNeeds equipmentNeeds = equipmentNeedsService.get(equipment.getEquipmentId());

                reservation.setEquipmentNeeds(equipmentNeeds);
                reservation.setQuantity(equipment.getQuantity());

                reservationRepository.save(reservation);

                ListEquipmentNeeds response = ListEquipmentNeeds.builder()
                        .id(reservation.getEquipmentNeeds().getId())
                        .name(reservation.getEquipmentNeeds().getName())
                        .quantity(reservation.getQuantity())
                        .build();
                equipments.add(response);
            }
        } else {
            reservationRepository.save(reservation);
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

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public ReservationResponse findById(String id) {
        Reservation reservationNotFound = reservationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        List<ListEquipmentNeeds> equipments = new ArrayList<>();

        if (reservationNotFound.getEquipmentNeeds() != null) {
            List<EquipmentNeeds> allById = equipmentNeedsService.getAllById(reservationNotFound.getEquipmentNeeds().getId());

            for (EquipmentNeeds equipmentNeeds : allById) {
                ListEquipmentNeeds response = ListEquipmentNeeds.builder()
                        .id(equipmentNeeds.getId())
                        .name(equipmentNeeds.getName())
                        .quantity(reservationNotFound.getQuantity())
                        .build();

                equipments.add(response);
            }
        }

        return ReservationResponse.builder()
                .id(reservationNotFound.getId())
                .employeeName(reservationNotFound.getEmployee().getName())
                .roomName(reservationNotFound.getRoom().getName())
                .reservationDate(reservationNotFound.getReservationDate())
                .status(reservationNotFound.getStatus().getDisplayValue())
                .equipmentNeeds(equipments)
                .build();
    }


    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public ReservationResponse update(ReservationRequest request) {
        utils.validate(request);

        Reservation reservationNotFound = reservationRepository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found")
        );


        Room roomById = roomService.get(request.getRoomId());

        Employee employee = employeeService.get(request.getEmployeeId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

        Date date = dateFormat.parse(request.getReservationDate());

        reservationNotFound.setRoom(roomById);
        reservationNotFound.setStatus(ETrans.PENDING);
        reservationNotFound.setEmployee(employee);

        List<ListEquipmentNeeds> equipments = new ArrayList<>();

        for (ListEquipment equipment : request.getEquipmentNeeds()){
            EquipmentNeeds equipmentNeeds = equipmentNeedsService.get(equipment.getEquipmentId());

            reservationNotFound.setEquipmentNeeds(equipmentNeeds);
            reservationNotFound.setQuantity(equipment.getQuantity());

            if (equipmentNeeds.getStock() - reservationNotFound.getQuantity() < 0){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity exceeds Stock");
            }

            equipmentNeeds.setStock(equipmentNeeds.getStock() - reservationNotFound.getQuantity());
            equipmentNeedsService.update(equipmentNeeds);

            reservationRepository.save(reservationNotFound);

            ListEquipmentNeeds response = ListEquipmentNeeds.builder()
                    .id(reservationNotFound.getEquipmentNeeds().getId())
                    .name(reservationNotFound.getEquipmentNeeds().getName())
                    .quantity(reservationNotFound.getQuantity())
                    .build();
            equipments.add(response);
        }


        return  ReservationResponse.builder()
                .id(reservationNotFound.getId())
                .employeeName(reservationNotFound.getEmployee().getName())
                .roomName(reservationNotFound.getRoom().getName())
                .reservationDate(date)
                .status(reservationNotFound.getStatus().getDisplayValue())
                .equipmentNeeds(equipments)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationResponse updateStatus(UpdateStatusReservation reservation) {

        utils.validate(reservation);

        Reservation reservationNotFound = reservationRepository.findById(reservation.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found")
        );

        switch (reservation.getStatus()){
            case "DECLINE":
                reservationNotFound.setStatus(ETrans.DECLINE);
                if (reservation.getRejectionReason().isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rejection reason not empty");

                reservationNotFound.setRejectionReason(reservation.getRejectionReason());

                UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                Approval approvalDecline = Approval.builder()
                        .approvalDate(new Date())
                        .acceptanceStatus(ETrans.DECLINE)
                        .approvedBy(currentUser.getUsername())
                        .reservation(reservationNotFound)
                        .build();

                approvalService.create(approvalDecline);
                reservationRepository.save(reservationNotFound);
                break;
            case "ACCEPT":
                reservationNotFound.setStatus(ETrans.ACCEPT);

                Room room = roomService.get(reservationNotFound.getRoom().getId());
                room.setStatus(ERoom.BOOKED);

                if (reservationNotFound.getEquipmentNeeds() != null){

                    EquipmentNeeds equipmentNeeds = equipmentNeedsService.get(reservationNotFound.getEquipmentNeeds().getId());

                    if (equipmentNeeds.getStock() - reservationNotFound.getQuantity() < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity exceeds Stock");
                    }

                    equipmentNeeds.setStock(equipmentNeeds.getStock() - reservationNotFound.getQuantity());
                    equipmentNeedsService.update(equipmentNeeds);
                }


                UserCredential currentUserApprove = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                Approval approvalAccept = Approval.builder()
                        .approvalDate(new Date())
                        .acceptanceStatus(ETrans.ACCEPT)
                        .approvedBy(currentUserApprove.getUsername())
                        .reservation(reservationNotFound)
                        .build();

                approvalService.create(approvalAccept);
                roomService.updateStatusRoom(room);
                reservationRepository.save(reservationNotFound);
                break;
            default:
                throw new IllegalArgumentException("Invalid status type: " + reservation.getStatus());
        }

        List<ListEquipmentNeeds> equipments = new ArrayList<>();

        if (reservationNotFound.getEquipmentNeeds() != null) {
            List<EquipmentNeeds> allById = equipmentNeedsService.getAllById(reservationNotFound.getEquipmentNeeds().getId());

            for (EquipmentNeeds equipmentNeeds : allById) {
                ListEquipmentNeeds response = ListEquipmentNeeds.builder()
                        .id(equipmentNeeds.getId())
                        .name(equipmentNeeds.getName())
                        .quantity(reservationNotFound.getQuantity())
                        .build();

                equipments.add(response);
            }
        }

        return ReservationResponse.builder()
                .id(reservationNotFound.getId())
                .employeeName(reservationNotFound.getEmployee().getName())
                .roomName(reservationNotFound.getRoom().getName())
                .reservationDate(reservationNotFound.getReservationDate())
                .status(reservationNotFound.getStatus().getDisplayValue())
                .equipmentNeeds(equipments)
                .build();
    }

    @Override
    public Page<ReservationResponse> search(SearchReservationRequest request) {

        Specification<Reservation> specification = getReservationSpecification(request);

        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<Reservation> all = reservationRepository.findAll(specification, pageable);

        List<ReservationResponse> responses = new ArrayList<>();

        for (Reservation reservation : all){

            List<ListEquipmentNeeds> equipments = new ArrayList<>();

            if (reservation.getEquipmentNeeds() != null) {

                List<EquipmentNeeds> allById = equipmentNeedsService.getAllById(reservation.getEquipmentNeeds().getId());

                for (EquipmentNeeds equipmentNeeds : allById) {
                    ListEquipmentNeeds response = ListEquipmentNeeds.builder()
                            .id(equipmentNeeds.getId())
                            .name(equipmentNeeds.getName())
                            .quantity(reservation.getQuantity())
                            .build();

                    equipments.add(response);
                }
            }

            ReservationResponse response = ReservationResponse.builder()
                    .id(reservation.getId())
                    .employeeName(reservation.getEmployee().getName())
                    .roomName(reservation.getRoom().getName())
                    .reservationDate(reservation.getReservationDate())
                    .status(reservation.getStatus().getDisplayValue())
                    .equipmentNeeds(equipments)
                    .build();

            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, all.getTotalElements());
    }

    @Override
    public List<ReservationResponse> getReservationByEmployeeId(String employeeId) {

        List<Reservation> byEmployeeId = reservationRepository.findByEmployeeId(employeeId);

        if (byEmployeeId.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST");

        List<ReservationResponse> responses = new ArrayList<>();

        for (Reservation reservation : byEmployeeId){

            List<ListEquipmentNeeds> equipments = new ArrayList<>();

            if (reservation.getEquipmentNeeds() != null) {
                List<EquipmentNeeds> allById = equipmentNeedsService.getAllById(reservation.getEquipmentNeeds().getId());

                for (EquipmentNeeds equipmentNeeds : allById) {
                    ListEquipmentNeeds response = ListEquipmentNeeds.builder()
                            .id(equipmentNeeds.getId())
                            .name(equipmentNeeds.getName())
                            .quantity(reservation.getQuantity())
                            .build();

                    equipments.add(response);
                }
            }

            ReservationResponse response = ReservationResponse.builder()
                    .id(reservation.getId())
                    .employeeName(reservation.getEmployee().getName())
                    .roomName(reservation.getRoom().getName())
                    .reservationDate(reservation.getReservationDate())
                    .status(reservation.getStatus().getDisplayValue())
                    .equipmentNeeds(equipments)
                    .build();

            responses.add(response);
        }

        return responses;
    }

    private Specification<Reservation> getReservationSpecification(SearchReservationRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getStartDate()) && Objects.nonNull(request.getEndDate())) {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    LocalDate startDate = LocalDate.parse(request.getStartDate(), dateFormatter);
                    LocalDate endDate = LocalDate.parse(request.getEndDate(), dateFormatter);


                    predicates.add(criteriaBuilder.between(
                            root.get("reservationDate"),
                            Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    ));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });
    }

}
