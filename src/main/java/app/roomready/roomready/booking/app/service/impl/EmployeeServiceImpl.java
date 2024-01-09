package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.entity.Employee;
import app.roomready.roomready.booking.app.repository.EmployeeRepository;
import app.roomready.roomready.booking.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Employee employee) {
        Optional<Employee> optionalEmployee = repository.findByName(employee.getName());

        if (optionalEmployee.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD Request");

        repository.save(employee);
    }
}
