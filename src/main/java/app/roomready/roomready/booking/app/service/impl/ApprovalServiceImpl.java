package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.configuration.CSVHelper;
import app.roomready.roomready.booking.app.dto.request.*;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponse;
import app.roomready.roomready.booking.app.dto.response.ListEquipmentNeeds;
import app.roomready.roomready.booking.app.dto.response.ReservationResponse;
import app.roomready.roomready.booking.app.entity.*;
import app.roomready.roomready.booking.app.repository.ApprovalRepository;
import app.roomready.roomready.booking.app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;

    private final EquipmentNeedsService equipmentNeedsService;

    @Override
    @Transactional(readOnly = true)
    public Page<ApprovalResponse> getAll(ApprovalRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize());

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Approval> all = approvalRepository.findAll(pageRequest);

        List<ApprovalResponse> responses = new ArrayList<>();

        for (Approval approval : all){

            List<ReservationResponse> reservationResponses = new ArrayList<>();

            List<ListEquipmentNeeds> equipments = new ArrayList<>();

            if (approval.getReservation().getEquipmentNeeds() != null) {

                List<EquipmentNeeds> allById = equipmentNeedsService.getAllById(approval.getReservation().getEquipmentNeeds().getId());

                for (EquipmentNeeds equipmentNeeds : allById) {
                    ListEquipmentNeeds response = ListEquipmentNeeds.builder()
                            .id(equipmentNeeds.getId())
                            .name(equipmentNeeds.getName())
                            .quantity(approval.getReservation().getQuantity())
                            .build();

                    equipments.add(response);
                }
            }

            ReservationResponse reservationResponse = ReservationResponse.builder()
                    .id(approval.getReservation().getId())
                    .employeeName(approval.getReservation().getEmployee().getName())
                    .roomName(approval.getReservation().getRoom().getName())
                    .reservationDate(approval.getReservation().getReservationDate())
                    .status(approval.getReservation().getStatus().getDisplayValue())
                    .equipmentNeeds(equipments)
                    .build();

            reservationResponses.add(reservationResponse);

            ApprovalResponse response = ApprovalResponse.builder()
                    .id(approval.getId())
                    .approvalDate(approval.getApprovalDate())
                    .approvalBy(approval.getApprovedBy())
                    .statusApproval(approval.getAcceptanceStatus().getDisplayValue())
                    .responses(reservationResponses)
                    .build();

            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, all.getTotalElements());
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
    public ByteArrayInputStream downloadCsv() {
        List<Approval> downloadCsv = approvalRepository.findAll();
        return CSVHelper.tutorialsToCSV(downloadCsv);
    }

    @Override
    public ByteArrayInputStream downloadApproval() {
        List<Approval> download = approvalRepository.download();
        return CSVHelper.approvalToCSV(download);
    }


}
