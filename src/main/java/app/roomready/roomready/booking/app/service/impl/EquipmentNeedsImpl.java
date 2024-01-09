package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.EquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.entity.EquipmentNeeds;
import app.roomready.roomready.booking.app.repository.EquipmentNeedsRepository;
import app.roomready.roomready.booking.app.service.EquipmentNeedsService;
import app.roomready.roomready.booking.app.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentNeedsImpl implements EquipmentNeedsService {

    private final EquipmentNeedsRepository repository;

    private final ValidationUtils utils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EquipmentNeedsResponse create(EquipmentRequest request) {

        utils.validate(request);

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
    @Transactional(readOnly = true)
    public EquipmentNeedsResponse getById(String id) {

        EquipmentNeeds equipmentNeeds = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );

        return toEquipmentNeedsResponse(equipmentNeeds);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public EquipmentNeeds get(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EquipmentNeedsResponse update(EquipmentRequest request) {

        utils.validate(request);

        EquipmentNeeds equipmentNeeds = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        equipmentNeeds.setName(request.getName());
        equipmentNeeds.setQuantity(request.getQuantity());

        repository.save(equipmentNeeds);

        return toEquipmentNeedsResponse(equipmentNeeds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        EquipmentNeeds equipmentNeeds = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        repository.delete(equipmentNeeds);
    }

    private EquipmentNeedsResponse toEquipmentNeedsResponse(EquipmentNeeds equipmentNeeds){
        return EquipmentNeedsResponse.builder()
                .id(equipmentNeeds.getId())
                .name(equipmentNeeds.getName())
                .quantity(equipmentNeeds.getQuantity())
                .build();
    }
}
