package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.dto.request.RoomRequest;
import app.roomready.roomready.booking.app.dto.request.RoomUpdateRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateRoomStatusRequest;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.RoomResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.dto.request.SearchRoomRequest;
import app.roomready.roomready.booking.app.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createNewRoom(@RequestBody RoomRequest request){
        RoomResponse roomResponse = roomService.createNew(request);

        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new room")
                .data(roomResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable("id") String id){
        RoomResponse roomResponse = roomService.getById(id);
        WebResponse<RoomResponse> response = WebResponse.<RoomResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get room by id")
                .data(roomResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minCapacities,
            @RequestParam(required = false) Integer maxCapacities,
            @RequestParam(required = false) ERoom status,
            @RequestParam(required = false) String facilities
    ){
        SearchRoomRequest request = SearchRoomRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .minCapacities(minCapacities)
                .maxCapacities(maxCapacities)
                .status(status)
                .facilities(facilities)
                .build();

        WebResponse<?> response = roomService.getAll(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteRoomById(@PathVariable("id") String id){
        roomService.deleteById(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete room")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateRoom(@PathVariable("id") String id,
            @RequestBody RoomUpdateRequest request){
        request.setId(id);
        RoomResponse updatedRoom = roomService.update(request);
        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update room")
                .data(updatedRoom)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GA')")
    public ResponseEntity<?> updateStatusRoom(@PathVariable("id") String id, @RequestBody UpdateRoomStatusRequest request){
        request.setId(id);
        RoomResponse roomResponse = roomService.updateStatus(request);
        WebResponse<RoomResponse> response = WebResponse.<RoomResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully change status room")
                .data(roomResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
