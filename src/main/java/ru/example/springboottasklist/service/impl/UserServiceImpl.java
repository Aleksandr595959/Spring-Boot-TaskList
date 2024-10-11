package ru.example.springboottasklist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboottasklist.dto.UpdateUserDto;
import ru.example.springboottasklist.dto.UserDto;
import ru.example.springboottasklist.entity.User;
import ru.example.springboottasklist.mapper.UserMapper;
import ru.example.springboottasklist.repository.UserRepository;
import ru.example.springboottasklist.service.UserService;
import ru.example.springboottasklist.utils.AuthUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthUtils authUtils;

    @Override
    public void setPassword(final String currentPassword, final String newPassword) {
        log.info("Was invoked method : setPassword");

        User user = authUtils.getUserFromAuthentication();
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    @Override
    public UserDto getAuthorizedUser() {
        log.info("Was invoked method for : getAuthorizedUser");

        return userMapper.toDto(authUtils.getUserFromAuthentication());
    }

    @Override
    public UpdateUserDto updateUser(final UpdateUserDto updateUser) {
        log.info("Was invoked method for : updateUser");

        User user = authUtils.getUserFromAuthentication();
        userMapper.updateUserFromDto(user, updateUser);
        var userUpdated = userRepository.save(user);
        return userMapper.updateUserDtoFromUser(userUpdated);
    }
}
