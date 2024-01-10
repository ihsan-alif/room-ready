package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/approval")
public class ApprovalController {

    private final ApprovalService approvalService;
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GA')")
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
    @PreAuthorize("hasAnyRole('ADMIN','GA')")
    public ResponseEntity<?> createApproval(@RequestBody Approval request){

        ApprovalResponse approvalResponse = approvalService.create(request);

        WebResponse<?> webResponse = WebResponse.builder()
                .message("Succes Create Approval")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .data(approvalResponse)
                .build();
        return ResponseEntity.ok(webResponse);
    }


    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GA')")
    public ResponseEntity<?> getApprovalById(@PathVariable String id){
        ApprovalResponse byId = approvalService.getById(id);
        return ResponseEntity.ok(byId);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','GA')")
    public ResponseEntity<?> updateApproval(@RequestBody Approval request){
        ApprovalResponse approval = approvalService.updateCustomer(request);
        return ResponseEntity.ok(approval);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GA')")
    public ResponseEntity<?> deleteApproval(@PathVariable String id){
        approvalService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Succes Delete Data By id");
    }
}
