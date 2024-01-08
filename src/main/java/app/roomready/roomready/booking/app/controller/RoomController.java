package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createNewRoom(@RequestBody Room room){
        Room newRoom = roomService.createNew(room);

        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new room")
                .data(newRoom)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
