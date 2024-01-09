package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.UpdateEmployeeRequest;
import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/{employeeId}")
    public ResponseEntity<WebResponse<EmployeeResponse>> getById(@PathVariable("employeeId") String id){
        EmployeeResponse byId = service.getById(id);

        WebResponse<EmployeeResponse> response = WebResponse.<EmployeeResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get by id employee")
                .data(byId)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<WebResponse<EmployeeResponse>> update(@PathVariable("employeeId") String id,
                                                                @RequestBody UpdateEmployeeRequest request){
        request.setId(id);

        EmployeeResponse updated = service.update(request);

        WebResponse<EmployeeResponse> response = WebResponse.<EmployeeResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update employee")
                .data(updated)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("employeeId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete employee")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
