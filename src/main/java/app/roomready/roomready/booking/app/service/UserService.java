package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.entity.UserCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserCredential loadUserById(String userId);
    UserCredential updateCredential(UserCredential userCredential);
}
