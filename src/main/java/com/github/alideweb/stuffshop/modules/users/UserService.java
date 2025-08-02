package com.github.alideweb.stuffshop.modules.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}
