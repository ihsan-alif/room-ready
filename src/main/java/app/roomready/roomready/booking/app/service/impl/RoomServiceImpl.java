package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.RoomRequest;
import app.roomready.roomready.booking.app.dto.request.RoomUpdateRequest;
import app.roomready.roomready.booking.app.dto.response.PagingResponse;
import app.roomready.roomready.booking.app.dto.response.RoomResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Room;
import app.roomready.roomready.booking.app.dto.request.SearchRoomRequest;
import app.roomready.roomready.booking.app.repository.RoomRepository;
import app.roomready.roomready.booking.app.service.RoomService;
import app.roomready.roomready.booking.app.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ValidationUtils validationUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomResponse createNew(RoomRequest request) {
        try {
            validationUtils.validate(request);
            Room room = Room.builder()
                    .name(request.getName())
                    .capacities(request.getCapacities())
                    .status(request.getStatus())
                    .facilities(request.getFacilities())
                    .build();
            room = roomRepository.save(room);
            return toRoomResponse(room);
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "room name already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getById(String id) {
        Room room = findByIdOrThrowNotFound(id);
        return toRoomResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public WebResponse<List<RoomResponse>> getAll(SearchRoomRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(
                (request.getPage() - 1), request.getSize()
        );

        Specification<Room> specification = getRoomSpecification(request);

        Page<Room> rooms = roomRepository.findAll(specification, pageable);

        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms.getContent()){
            RoomResponse roomResponse = toRoomResponse(room);
            roomResponses.add(roomResponse);
        }

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(request.getSize())
                .totalPage(rooms.getTotalPages())
                .totalElements(rooms.getTotalElements())
                .build();

        return WebResponse.<List<RoomResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get all room")
                .paging(pagingResponse)
                .data(roomResponses)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomResponse update(RoomUpdateRequest request) {
        validationUtils.validate(request);

        Room room = findByIdOrThrowNotFound(request.getId());
        room.setId(request.getId());
        room.setName(request.getName());
        room.setCapacities(request.getCapacities());
        room.setStatus(request.getStatus());
        room.setFacilities(request.getFacilities());
        roomRepository.saveAndFlush(room);

        return toRoomResponse(room);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        Room room = findByIdOrThrowNotFound(id);
        roomRepository.delete(room);
    }

    private Room findByIdOrThrowNotFound(String id){
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "room not found"));
    }

    private RoomResponse toRoomResponse(Room room){
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .capacities(room.getCapacities())
                .status(room.getStatus())
                .facilities(room.getFacilities())
                .build();
    }

    private Specification<Room> getRoomSpecification(SearchRoomRequest request){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null){
                Predicate predicateName = criteriaBuilder.like(root.get("name"), "%" + request.getName().toLowerCase() + "%");
                predicates.add(predicateName);
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
