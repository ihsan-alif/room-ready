package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.CreateEquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.entity.EquipmentNeeds;

public interface EquipmentNeedsService {

    EquipmentNeedsResponse create(CreateEquipmentRequest request);

    EquipmentNeedsResponse getById(String id);

    EquipmentNeeds get(String id);
}
