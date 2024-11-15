package ru.example.springboottasklist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboottasklist.dto.RegisterDto;
import ru.example.springboottasklist.entity.User;
import ru.example.springboottasklist.mapper.UserMapper;
import ru.example.springboottasklist.repository.UserRepository;
import ru.example.springboottasklist.service.AuthService;

/**
 * Сервис аутентификации и авторизации.
 * Осуществляет процессы входа и регистрации пользователей.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    /**
     * Метод для входа пользователя.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return true, если вход успешный; false, если имя пользователя не найдено или пароль неверный
     */
    @Override
    public boolean login(final String username,final String password) {
        log.info("Was invoked method for : login");

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return passwordEncoder.matches(password, userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    /**
     * Метод для регистрации пользователя.
     *
     * @param registerDto данные для регистрации
     * @return true, если регистрация успешная; false, если имя пользователя уже занято
     */
    @Override
    public boolean register(final RegisterDto registerDto) {
        log.info("Was invoked method for : register");

        if (userRepository.existsByLogin(registerDto.username())) {
            return false;
        }

        User user = userMapper.toEntity(registerDto);
        userRepository.save(user);
        return true;
    }


}
