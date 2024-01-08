package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.model.SearchRoomRequest;
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createNewRoom(@RequestBody Room room){
        Room newRoom = roomService.createNew(room);

        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new room")
                .data(newRoom)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/bulk")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createRoomBulk(@RequestBody List<Room> rooms){
        List<Room> bulk = roomService.createBulk(rooms);

        WebResponse<List<Room>> response = WebResponse.<List<Room>>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create room bulk")
                .data(bulk)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getRoomById(@RequestParam String id){
        Room room = roomService.getById(id);
        WebResponse<Room> response = WebResponse.<Room>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get room by id")
                .data(room)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name
    ){
        SearchRoomRequest request = SearchRoomRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .build();
        Page<Room> allRoom = roomService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPage(allRoom.getTotalPages())
                .totalElements(allRoom.getTotalElements())
                .build();

        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get all room")
                .paging(pagingResponse)
                .data(allRoom.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteRoomById(@RequestParam String id){
        roomService.deleteById(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete room")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
