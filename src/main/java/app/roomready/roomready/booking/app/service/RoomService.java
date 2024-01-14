package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.RoomRequest;
import app.roomready.roomready.booking.app.dto.request.RoomUpdateRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateRoomStatusRequest;
import app.roomready.roomready.booking.app.dto.response.RoomResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.dto.request.SearchRoomRequest;

import java.util.List;

public interface RoomService {
    RoomResponse createNew(RoomRequest request);

    RoomResponse getById(String id);

    WebResponse<List<RoomResponse>> getAll(SearchRoomRequest request);

    RoomResponse update(RoomUpdateRequest request);

    void deleteById(String id);

    RoomResponse updateStatus(UpdateRoomStatusRequest request);

    Room get(String id);

    Room updateStatusRoom(Room room);
}
