package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.entity.Employee;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.EmployeeRepository;
import app.roomready.roomready.booking.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    private static final String ROLE = "ROLE_ADMIN";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Employee employee) {
        Optional<Employee> optionalEmployee = repository.findByName(employee.getName());

        if (optionalEmployee.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD Request");

        repository.save(employee);
    }

    @Override
    public EmployeeResponse getById(String id) {

        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");

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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");

        return employee;
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
