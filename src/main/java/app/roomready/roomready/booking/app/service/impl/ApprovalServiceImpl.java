package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.dto.response.ApprovalResponse;
import app.roomready.roomready.booking.app.entity.Approval;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.exception.ErrorController;
import app.roomready.roomready.booking.app.repository.ApprovalRepository;
import app.roomready.roomready.booking.app.service.ApprovalService;
import app.roomready.roomready.booking.app.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;

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
    public ApprovalResponse getById(String request) {
        Optional<Approval> byIdResult = approvalRepository.findById(request);
        return ApprovalResponse.builder()
                .date(byIdResult.get().getApproval().toString())
                .id(byIdResult.get().getId())
                .name(byIdResult.get().getReservation().getRoom().getName())
                .status(byIdResult.get().getStatus())
                .acceptance(byIdResult.get().getRejection())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResponse create(ApprovalRequest request) {
        Approval approvalById = approvalRepository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not Found")
        );


        Approval save = approvalRepository.save(approvalById);

        UserCredential userCredential = userService.loadUserById(request.getIdName());
        return ApprovalResponse.builder()
                .date(save.getApproval().toString())
                .id(save.getId())
                .name(userCredential.getUsername())
                .status(save.getStatus())
                .acceptance(save.getRejection())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String request) {
        Optional<Approval> byIdResult = approvalRepository.findById(request);
        if (byIdResult.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not Found");
        approvalRepository.deleteById(request);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ApprovalResponse updateCustomer(Approval request) {
//        Optional<Approval> byIdFind = approvalRepository.findById(request.getId());
//        if (byIdFind.isEmpty()) throw new RuntimeException("Can not find Data");
//        return ApprovalResponse.builder()
//                .date(request.getApproval().toString())
//                .id(request.getId())
//                .name(request.getReservation().getRoom().getName())
//                .status(request.getStatus())
//                .acceptance(request.getRejection())
//                .build();
//
//    }

//    private static Specification<Approval> getApprovalSpecification(ApprovalRequest request) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            if (request.getStatus() != null){
//                Predicate addNamePredicate =  criteriaBuilder.equal(root.get("status"), request.getStatus());
//                predicates.add(addNamePredicate);
//            }
//            return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
//        };
//    }
}
