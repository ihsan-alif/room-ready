package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.CreateEquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.entity.EquipmentNeeds;
import app.roomready.roomready.booking.app.repository.EquipmentNeedsRepository;
import app.roomready.roomready.booking.app.service.EquipmentNeedsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentNeedsImpl implements EquipmentNeedsService {

    private final EquipmentNeedsRepository repository;

    @Override
    public EquipmentNeedsResponse create(CreateEquipmentRequest request) {

        Optional<EquipmentNeeds> optionalEquipmentNeeds = repository.findByName(request.getName());

        if (optionalEquipmentNeeds.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already exist");

        EquipmentNeeds equipmentNeeds = EquipmentNeeds.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .build();

        repository.save(equipmentNeeds);

        return toEquipmentNeedsResponse(equipmentNeeds);
    }

    @Override
    public EquipmentNeedsResponse getById(String id) {

        EquipmentNeeds equipmentNeeds = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );

        return toEquipmentNeedsResponse(equipmentNeeds);
    }

    @Override
    public EquipmentNeeds get(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );
    }

    private EquipmentNeedsResponse toEquipmentNeedsResponse(EquipmentNeeds equipmentNeeds){
        return EquipmentNeedsResponse.builder()
                .id(equipmentNeeds.getId())
                .name(equipmentNeeds.getName())
                .quantity(equipmentNeeds.getQuantity())
                .build();
    }
}
