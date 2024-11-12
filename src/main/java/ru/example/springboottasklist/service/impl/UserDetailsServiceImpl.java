package ru.example.springboottasklist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboottasklist.repository.UserRepository;

import java.util.Collections;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Was invoked method for : loadUserByUsername");

        final var user = userRepository.findByLogin(username).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь не найден: " + username));
        return new User(user.getLogin(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
