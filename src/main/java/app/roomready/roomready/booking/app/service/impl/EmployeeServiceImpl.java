package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.SearchEmployeeRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateEmployeeRequest;
import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.entity.Employee;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.EmployeeRepository;
import app.roomready.roomready.booking.app.service.EmployeeService;
import app.roomready.roomready.booking.app.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    private final UserService userService;

    private static final String ROLE = "ROLE_ADMIN";

    private static final String FORBIDDEN = "FORBIDDEN";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Employee employee) {
        repository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(String id) {

        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        return toEmployeeResponse(employee);
    }


    @Override
    public Employee get(String id) {
        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        return employee;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeResponse update(UpdateEmployeeRequest request) {
        Employee employee = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        employee.setName(request.getName());
        employee.setDivision(request.getDivision());
        employee.setPosition(request.getPosition());
        employee.setContactInfo(request.getContactInfo());

        repository.save(employee);

        return toEmployeeResponse(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        userService.delete(userCredential.getId());
        repository.delete(employee);
    }

    @Override
    public Page<EmployeeResponse> search(SearchEmployeeRequest request) {
        Specification<Employee> specification = getEmployeeSpecification(request);

        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Employee> all = repository.findAll(specification, pageable);

        List<EmployeeResponse> responses = new ArrayList<>();

        for (Employee employee : all){
            EmployeeResponse response = toEmployeeResponse(employee);

            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, all.getTotalElements());
    }

    private static Specification<Employee> getEmployeeSpecification(SearchEmployeeRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%"+ request.getName()+"%"));
            }

            if (Objects.nonNull(request.getDivision())){
                predicates.add(criteriaBuilder.like(root.get("division"), "%"+ request.getDivision()+"%"));
            }

            if (Objects.nonNull(request.getPosition())){
                predicates.add(criteriaBuilder.like(root.get("position"), "%"+ request.getPosition()+"%"));
            }

            if (Objects.nonNull(request.getContactInfo())){
                predicates.add(criteriaBuilder.like(root.get("contactInfo"), "%"+ request.getContactInfo()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }


    private EmployeeResponse toEmployeeResponse(Employee employee){
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .division(employee.getDivision())
                .position(employee.getPosition())
                .contactInfo(employee.getContactInfo())
                .username(employee.getUserCredential().getUsername())
                .build();
    }
}
