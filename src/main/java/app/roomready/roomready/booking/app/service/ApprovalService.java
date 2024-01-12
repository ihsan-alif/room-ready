package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.dto.request.ApprovalRequestReservation;
import app.roomready.roomready.booking.app.dto.request.ReservationRequest;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponse;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponseReservation;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.entity.Approval;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;

public interface ApprovalService {

    Page<Approval> getAll (ApprovalRequest request);

    Approval getById(String request);

//    void create(ApprovalRequest request);

    void deleteById(String request);

    void updateStatus(ApprovalRequestReservation request);

    ByteArrayInputStream downloadCsv();
}
