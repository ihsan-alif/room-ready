package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.EquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.entity.EquipmentNeeds;

public interface EquipmentNeedsService {

    EquipmentNeedsResponse create(EquipmentRequest request);

    EquipmentNeedsResponse getById(String id);

    EquipmentNeeds get(String id);

    EquipmentNeedsResponse update(EquipmentRequest request);

    void delete(String id);
}
