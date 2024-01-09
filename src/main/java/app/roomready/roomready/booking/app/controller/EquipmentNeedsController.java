package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.EquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.EquipmentNeedsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipment-needs")
public class EquipmentNeedsController {

    private final EquipmentNeedsService service;

    @PostMapping("/")
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
    public ResponseEntity<WebResponse<EquipmentNeedsResponse>> getById(@PathVariable("id") String id){
        EquipmentNeedsResponse byId = service.getById(id);

        WebResponse<EquipmentNeedsResponse> response = WebResponse.<EquipmentNeedsResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get by id equipment needs")
                .data(byId)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<EquipmentNeedsResponse>> update(@PathVariable("id") String id,
                                                                      @RequestBody EquipmentRequest request){
        request.setId(id);

        EquipmentNeedsResponse update = service.update(request);

        WebResponse<EquipmentNeedsResponse> response = WebResponse.<EquipmentNeedsResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update equipment needs")
                .data(update)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("id") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete equipment needs")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
