package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.CreateEquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.EquipmentNeedsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/equipment-needs")
public class EquipmentNeedsController {

    private final EquipmentNeedsService service;

    @PostMapping("/")
    public ResponseEntity<WebResponse<EquipmentNeedsResponse>> create(@RequestBody CreateEquipmentRequest request){
        EquipmentNeedsResponse equipmentNeedsResponse = service.create(request);

        WebResponse<EquipmentNeedsResponse> response = WebResponse.<EquipmentNeedsResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new equipment needs")
                .data(equipmentNeedsResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
