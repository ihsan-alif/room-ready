package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.LoginRequest;
import app.roomready.roomready.booking.app.dto.request.UserRegisterRequest;
import app.roomready.roomready.booking.app.dto.response.LoginResponse;
import app.roomready.roomready.booking.app.dto.response.RegisterResonse;

public interface AuthService {
    RegisterResonse register(UserRegisterRequest request);
    RegisterResonse registerAdmin(UserRegisterRequest request);
    RegisterResonse registerGA(UserRegisterRequest request);
    LoginResponse login(LoginRequest request);
}
