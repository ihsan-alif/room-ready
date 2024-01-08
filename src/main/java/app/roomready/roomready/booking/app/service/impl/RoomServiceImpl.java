package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.repository.RoomRepository;
import app.roomready.roomready.booking.app.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public Room createNew(Room room) {
        return roomRepository.save(room);
    }
}
