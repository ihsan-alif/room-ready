package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponse;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.entity.Approval;
import org.springframework.data.domain.Page;

public interface ApprovalService {

    Page<Approval> getAll (ApprovalRequest request);

    ApprovalResponse getById(String request);

    void create(Approval request);

    void deleteById(String request);

//    ApprovalResponse updateCustomer(ApprovalRequest customer);
}
