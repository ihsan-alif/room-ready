package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<WebResponse<EmployeeResponse>> getById(@PathVariable("employeeId") String id){
        EmployeeResponse byId = service.getById(id);

        WebResponse<EmployeeResponse> response = WebResponse.<EmployeeResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get by id employee")
                .data(byId)
                .build();

        return ResponseEntity.ok(response);
    }
}
