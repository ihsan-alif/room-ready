package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.constant.ERole;
import app.roomready.roomready.booking.app.entity.Role;

public interface RoleService {
    Role getOrSave(ERole role);
}
