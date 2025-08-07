package com.github.alideweb.stuffshop.modules.user;

import com.github.alideweb.stuffshop.exceptions.UserNotFoundException;
import com.github.alideweb.stuffshop.modules.user.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user with username: " + username + " not found"));
    }
}
