package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.constant.ERole;
import app.roomready.roomready.booking.app.dto.request.LoginRequest;
import app.roomready.roomready.booking.app.dto.request.UserRegisterRequest;
import app.roomready.roomready.booking.app.dto.response.LoginResponse;
import app.roomready.roomready.booking.app.dto.response.RegisterResponse;
import app.roomready.roomready.booking.app.entity.Role;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.UserCredentialRepository;
import app.roomready.roomready.booking.app.security.JwtUtils;
import app.roomready.roomready.booking.app.service.AuthService;
import app.roomready.roomready.booking.app.service.RoleService;
import app.roomready.roomready.booking.app.utils.ValidationUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtils validationUtils;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void initAdmin() {
        Optional<UserCredential> optionalUser = userCredentialRepository.findByUsername("initadmin");

        if (optionalUser.isPresent()) return;

        Role roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);
        Role roleGA = roleService.getOrSave(ERole.ROLE_GA);
        Role roleEmployee = roleService.getOrSave(ERole.ROLE_EMPLOYEE);

        String hashPassword = passwordEncoder.encode("password");

        UserCredential user = UserCredential.builder()
                .username("initadmin")
                .password(hashPassword)
                .roles(List.of(roleAdmin, roleGA, roleEmployee))
                .build();
        userCredentialRepository.saveAndFlush(user);
    }

    @Override
    public RegisterResponse register(UserRegisterRequest request) {
        validationUtils.validate(request);
        Role roleEmployee = roleService.getOrSave(ERole.ROLE_EMPLOYEE);

        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserCredential user = UserCredential.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .roles(List.of(roleEmployee))
                .build();
        userCredentialRepository.saveAndFlush(user);

        //build employee

        return toRegisterResponse(user);
    }

    @Override
    public RegisterResponse registerAdmin(UserRegisterRequest request) {
        validationUtils.validate(request);
        Role roleEmployee = roleService.getOrSave(ERole.ROLE_EMPLOYEE);
        Role roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);
        Role roleGA = roleService.getOrSave(ERole.ROLE_GA);

        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserCredential user = UserCredential.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .roles(List.of(roleEmployee, roleAdmin, roleGA))
                .build();
        userCredentialRepository.saveAndFlush(user);

        //build employee

        return toRegisterResponse(user);
    }

    @Override
    public RegisterResponse registerGA(UserRegisterRequest request) {
        validationUtils.validate(request);
        Role roleGA = roleService.getOrSave(ERole.ROLE_GA);

        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserCredential user = UserCredential.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .roles(List.of(roleGA))
                .build();
        userCredentialRepository.saveAndFlush(user);

        //build employee

        return toRegisterResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        validationUtils.validate(request);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserCredential user = (UserCredential) authenticate.getPrincipal();

        String token = jwtUtils.generateToken(user);
        List<String> roles = user.getRoles().stream().map(role -> role.getRole().name()).toList();

        return LoginResponse.builder()
                .token(token)
                .role(roles)
                .build();
    }

    private static RegisterResponse toRegisterResponse(UserCredential userCredential) {
        List<String> roles = userCredential.getRoles().stream().map(role -> role.getRole().name()).toList();
        return RegisterResponse.builder()
                .username(userCredential.getUsername())
                .role(roles)
                .build();
    }
}
