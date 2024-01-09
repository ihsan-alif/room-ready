package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.constant.ERole;
import app.roomready.roomready.booking.app.entity.Role;
import app.roomready.roomready.booking.app.repository.RoleRepository;
import app.roomready.roomready.booking.app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role getOrSave(ERole eRole) {
        Optional<Role> optionalRole = roleRepository.findByRole(eRole);
        if (optionalRole.isPresent()) return optionalRole.get();

        Role role = Role.builder()
                .role(eRole)
                .build();
        return roleRepository.save(role);
    }
}
