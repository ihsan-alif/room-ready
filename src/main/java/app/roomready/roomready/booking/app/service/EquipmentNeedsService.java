package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.CreateEquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;

public interface EquipmentNeedsService {

    EquipmentNeedsResponse create(CreateEquipmentRequest request);
}
