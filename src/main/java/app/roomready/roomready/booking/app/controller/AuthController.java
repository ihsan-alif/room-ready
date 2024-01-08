package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.LoginRequest;
import app.roomready.roomready.booking.app.dto.request.UserRegisterRequest;
import app.roomready.roomready.booking.app.dto.response.LoginResponse;
import app.roomready.roomready.booking.app.dto.response.RegisterResonse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        RegisterResonse register = authService.register(request);
        WebResponse<RegisterResonse> response = WebResponse.<RegisterResonse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new user")
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(path = "/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRegisterRequest request) {
        RegisterResonse register = authService.registerAdmin(request);
        WebResponse<RegisterResonse> response = WebResponse.<RegisterResonse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new user")
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(path = "/register/ga")
    public ResponseEntity<?> registerGA(@RequestBody UserRegisterRequest request) {
        RegisterResonse register = authService.registerGA(request);
        WebResponse<RegisterResonse> response = WebResponse.<RegisterResonse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new user")
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse login = authService.login(request);
        WebResponse<LoginResponse> response = WebResponse.<LoginResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully login")
                .data(login)
                .build();
        return ResponseEntity.ok(response);
    }
}
