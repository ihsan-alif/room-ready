package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Approval;
import app.roomready.roomready.booking.app.entity.Reservation;
import app.roomready.roomready.booking.app.service.ApprovalService;
import app.roomready.roomready.booking.app.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/approval")
public class ApprovalController {

    private final ApprovalService approvalService;
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size){

        ApprovalRequest approvalRequest = ApprovalRequest.builder()
                .size(size)
                .page(page)
                .build();

        Page<Approval> all = approvalService.getAll(approvalRequest);
        PagingResponse pagingResponse = PagingResponse.builder()
                .page(approvalRequest.getPage())
                .size(approvalRequest.getPage())
                .totalPage(all.getTotalPages())
                .totalElements(all.getTotalElements())
                .build();

        WebResponse<?> webResponse = WebResponse.builder()
        .status(HttpStatus.OK.getReasonPhrase())
                .message("Success Get All Data")
                .paging(pagingResponse)
                .data(all.getContent())
        .build();
        return ResponseEntity.ok(webResponse);
    }

    @PostMapping
    public ResponseEntity<?> createApproval(@RequestBody Approval request){

        Approval approval = approvalService.create(request);

        WebResponse<?> webResponse = WebResponse.builder()
                .message("Succes Create Approval")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .data(approval)
                .build();
        return ResponseEntity.ok(webResponse);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getApprovalById(@PathVariable String id){
        Approval byId = approvalService.getById(id);
        return ResponseEntity.ok(byId);
    }

    @PutMapping
    public ResponseEntity<?> updateApproval(@RequestBody Approval request){
        Approval approval = approvalService.updateCustomer(request);
        return ResponseEntity.ok(approval);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteApproval(@PathVariable String request){
        String deleteById = approvalService.deleteById(request);
        return ResponseEntity.ok(deleteById);
    }
}
