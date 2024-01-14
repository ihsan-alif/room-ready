package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.entity.UserCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserCredential loadUserById(String userId);
    void updateCredential(UserCredential userCredential);
    void delete(String id);
}
