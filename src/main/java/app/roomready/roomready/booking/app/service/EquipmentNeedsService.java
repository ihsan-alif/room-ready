package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.EquipmentRequest;
import app.roomready.roomready.booking.app.dto.request.SearchEquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.entity.EquipmentNeeds;
import org.springframework.data.domain.Page;

public interface EquipmentNeedsService {

    EquipmentNeedsResponse create(EquipmentRequest request);

    EquipmentNeedsResponse getById(String id);

    EquipmentNeeds get(String id);

    EquipmentNeedsResponse update(EquipmentNeeds request);

    void delete(String id);

    Page<EquipmentNeedsResponse> search(SearchEquipmentRequest request);
}
