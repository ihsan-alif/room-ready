package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.RoomRequest;
import app.roomready.roomready.booking.app.dto.response.RoomResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.dto.request.SearchRoomRequest;

import java.util.List;

public interface RoomService {
    RoomResponse createNew(RoomRequest request);
    RoomResponse getById(String id);
    WebResponse<List<RoomResponse>> getAll(SearchRoomRequest request);
    RoomResponse update(Room room);
    void deleteById(String id);
}
