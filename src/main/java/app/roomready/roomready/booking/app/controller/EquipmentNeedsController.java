package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.EquipmentRequest;
import app.roomready.roomready.booking.app.dto.request.SearchEquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.EquipmentNeedsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/equipment-needs")
public class EquipmentNeedsController {

    private final EquipmentNeedsService service;

    @PostMapping(path = "/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<EquipmentNeedsResponse>> create(@RequestBody EquipmentRequest request){
        EquipmentNeedsResponse equipmentNeedsResponse = service.create(request);

        WebResponse<EquipmentNeedsResponse> response = WebResponse.<EquipmentNeedsResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new equipment needs")
                .data(equipmentNeedsResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<EquipmentNeedsResponse>> getById(@PathVariable("id") String id){
        EquipmentNeedsResponse byId = service.getById(id);

        WebResponse<EquipmentNeedsResponse> response = WebResponse.<EquipmentNeedsResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get by id equipment needs")
                .data(byId)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("id") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete equipment needs")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<List<EquipmentNeedsResponse>>> search(@RequestParam(name = "name", required = false) String name,
                                                                            @RequestParam(name = "stock", required = false) Long stock,
                                                                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                            @RequestParam(name = "size", defaultValue = "10") Integer size){
        SearchEquipmentRequest request = SearchEquipmentRequest.builder()
                .name(name)
                .stock(stock)
                .page(page)
                .size(size)
                .build();

        Page<EquipmentNeedsResponse> search = service.search(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPage(search.getTotalPages())
                .totalElements(search.getTotalElements())
                .build();

        WebResponse<List<EquipmentNeedsResponse>> response = WebResponse.<List<EquipmentNeedsResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get equipment needs")
                .data(search.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
