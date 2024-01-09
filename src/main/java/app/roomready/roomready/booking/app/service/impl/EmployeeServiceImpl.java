package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.UpdateEmployeeRequest;
import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.entity.Employee;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.EmployeeRepository;
import app.roomready.roomready.booking.app.service.EmployeeService;
import app.roomready.roomready.booking.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
