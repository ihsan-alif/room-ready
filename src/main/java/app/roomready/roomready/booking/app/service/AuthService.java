package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.LoginRequest;
import app.roomready.roomready.booking.app.dto.request.UserRegisterRequest;
import app.roomready.roomready.booking.app.dto.response.LoginResponse;
import app.roomready.roomready.booking.app.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(UserRegisterRequest request);
    RegisterResponse registerAdmin(UserRegisterRequest request);
    RegisterResponse registerGA(UserRegisterRequest request);
    LoginResponse login(LoginRequest request);
}
