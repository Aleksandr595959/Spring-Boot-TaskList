package ru.example.springboottasklist.service;
import ru.example.springboottasklist.dto.RegisterDto;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(RegisterDto registerDto);
}
