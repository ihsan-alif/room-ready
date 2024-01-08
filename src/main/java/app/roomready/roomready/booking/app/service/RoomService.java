package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.model.SearchRoomRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {
    Room createNew(Room room);
    Room getById(String id);
    List<Room> createBulk(List<Room> rooms);
    Page<Room> getAll(SearchRoomRequest request);
    Room update(Room room);
    void deleteById(String id);
}
