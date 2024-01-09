package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.UserCredentialRepository;
import app.roomready.roomready.booking.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;
    @Override
    public UserCredential loadUserById(String userId) {
        return userCredentialRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));
    }

    @Override
    public UserCredential updateCredential(UserCredential userCredential) {
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findById(userCredential.getId());
        if (optionalUserCredential.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found");

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential credential = optionalUserCredential.get();

        if (!currentUser.getId().equals(credential.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "forbidden");

        return userCredentialRepository.save(userCredential);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        UserCredential user = userCredentialRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found")
        );

        userCredentialRepository.delete(user);
    }
}
