package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.SearchEmployeeRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateEmployeeRequest;
import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("employeeId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete employee")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<List<EmployeeResponse>>> search(@RequestParam(name = "name", required = false) String name,
                                                                      @RequestParam(name = "division", required = false) String division,
                                                                      @RequestParam(name = "position", required = false) String position,
                                                                      @RequestParam(name = "contactInfo", required = false) String contactInfo,
                                                                      @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                      @RequestParam(name = "size", defaultValue = "10") Integer size){
        SearchEmployeeRequest request = SearchEmployeeRequest.builder()
                .name(name)
                .division(division)
                .position(position)
                .contactInfo(contactInfo)
                .page(page)
                .size(size)
                .build();

        Page<EmployeeResponse> search = service.search(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPage(search.getTotalPages())
                .totalElements(search.getTotalElements())
                .build();

        WebResponse<List<EmployeeResponse>> response = WebResponse.<List<EmployeeResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get employee")
                .data(search.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
