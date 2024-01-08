package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.ApprovalRequest;
import app.roomready.roomready.booking.app.entity.Approval;
import app.roomready.roomready.booking.app.exception.ErrorController;
import app.roomready.roomready.booking.app.repository.ApprovalRepository;
import app.roomready.roomready.booking.app.service.ApprovalService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    @Override
    public Page<Approval> getAll(ApprovalRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(
                (request.getPage() - 1), request.getSize());
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
//        Specification<Approval> approvalSpecification = getApprovalSpecification(request);
        return approvalRepository.findAll(pageRequest);
    }

    @Override
    public Approval getById(String request) {
        Optional<Approval> byIdResult = approvalRepository.findById(request);
        return byIdResult.orElse(null);
    }

    @Override
    public Approval create(Approval request) {
        return approvalRepository.save(request);
    }

    @Override
    public String deleteById(String request) {
        Optional<Approval> byIdResult = approvalRepository.findById(request);
        if (byIdResult.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not Found");
        approvalRepository.deleteById(request);
        return "Succes Delete data";
    }

    @Override
    public Approval updateCustomer(Approval request) {
        Optional<Approval> byIdFind = approvalRepository.findById(request.getId());
        if (byIdFind.isEmpty())throw new RuntimeException("Can not find Data");
        return approvalRepository.save(request);

    }

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
