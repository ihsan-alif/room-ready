package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.model.SearchRoomRequest;
import app.roomready.roomready.booking.app.repository.RoomRepository;
import app.roomready.roomready.booking.app.service.RoomService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public Room createNew(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getById(String id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) return roomRepository.findById(id).get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "room not found");
    }

    @Override
    public List<Room> createBulk(List<Room> rooms) {
        return  roomRepository.saveAll(rooms);
    }

    @Override
    public Page<Room> getAll(SearchRoomRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(
                (request.getPage() - 1), request.getSize()
        );

        Specification<Room> specification = getRoomSpecification(request);

        return roomRepository.findAll(specification, pageable);
    }

    @Override
    public Room update(Room room) {
        Optional<Room> optionalRoom = roomRepository.findById(room.getId());
        if (optionalRoom.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "room not found");
        return roomRepository.save(room);
    }

    @Override
    public void deleteById(String id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "room not found");
        roomRepository.delete(optionalRoom.get());
    }

    private Specification<Room> getRoomSpecification(SearchRoomRequest request){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null){
                Predicate predicateName = criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%");
                predicates.add(predicateName);
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
