package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponse;
import app.roomready.roomready.booking.app.entity.Approval;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;

public interface ApprovalService {

    Page<ApprovalResponse> getAll (ApprovalRequest request);

    Approval getById(String request);

    void create(Approval request);

    void deleteById(String request);

    ByteArrayInputStream downloadCsv();

    ByteArrayInputStream downloadApproval(String startDate, String endDate);
}
