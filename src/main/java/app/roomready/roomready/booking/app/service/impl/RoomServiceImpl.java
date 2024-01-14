package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.constant.ERoom;
import app.roomready.roomready.booking.app.dto.request.RoomRequest;
import app.roomready.roomready.booking.app.dto.request.RoomUpdateRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateRoomStatusRequest;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
                    .status(ERoom.valueOf(request.getStatus().toUpperCase()))
                    .facilities(request.getFacilities())
                    .build();
            room = roomRepository.saveAndFlush(room);
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
        try {
            validationUtils.validate(request);
            Room room = findByIdOrThrowNotFound(request.getId());
            room.setId(request.getId());
            room.setName(request.getName());
            room.setCapacities(request.getCapacities());
            room.setStatus(ERoom.valueOf(request.getStatus().toUpperCase()));
            room.setFacilities(request.getFacilities());
            roomRepository.saveAndFlush(room);
            return toRoomResponse(room);
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "room name already exist");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        Room room = findByIdOrThrowNotFound(id);
        roomRepository.delete(room);
    }

    @Override
    public RoomResponse updateStatus(UpdateRoomStatusRequest request) {
        validationUtils.validate(request);
        Room room = findByIdOrThrowNotFound(request.getId());
        room.setStatus(ERoom.valueOf(request.getStatus().toUpperCase()));
        room = roomRepository.saveAndFlush(room);
        return toRoomResponse(room);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Room get(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Room updateStatusRoom(Room room) {

        Room roomStatus = findByIdOrThrowNotFound(room.getId());

        roomStatus.setStatus(room.getStatus());

        return roomRepository.save(roomStatus);
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
                .status(room.getStatus().getDisplayValue())
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
            if (request.getMinCapacities() != null){
                Predicate predicateMinCapacities = criteriaBuilder.greaterThanOrEqualTo(root.get("capacities"), request.getMinCapacities());
                predicates.add(predicateMinCapacities);
            }
            if (request.getMaxCapacities() != null){
                Predicate predicateMaxCapacities = criteriaBuilder.lessThanOrEqualTo(root.get("capacities"), request.getMaxCapacities());
                predicates.add(predicateMaxCapacities);
            }
            if(request.getStatus() != null){
                Predicate predicateStatus = criteriaBuilder.equal(root.get("status"), request.getStatus());
                predicates.add(predicateStatus);
            }
            if (request.getFacilities() != null){
                String[] facilitiesArray = request.getFacilities().split(", ");

                List<Predicate> facilities = Arrays.stream(facilitiesArray)
                        .map(facility -> criteriaBuilder.like(root.get("facilities"), "%" + facility.trim() + "%"))
                        .collect(Collectors.toList());

                Predicate predicateFacilities = criteriaBuilder.or(facilities.toArray(new Predicate[0]));
                predicates.add(predicateFacilities);
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
