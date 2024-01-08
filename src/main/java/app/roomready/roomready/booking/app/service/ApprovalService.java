package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.entity.Approval;
import org.springframework.data.domain.Page;

public interface ApprovalService {

    Page<Approval> getAll (ApprovalRequest request);

    Approval getById(String request);

    Approval create(Approval request);

    String deleteById(String request);

    Approval updateCustomer(Approval customer);
}
