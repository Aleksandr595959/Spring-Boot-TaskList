package ru.example.springboottasklist.service;


import ru.example.springboottasklist.dto.UpdateUserDto;
import ru.example.springboottasklist.dto.UserDto;

public interface UserService {

    void setPassword(String currentPassword, String newPassword);

    UserDto getAuthorizedUser();

    UpdateUserDto updateUser(UpdateUserDto updateUser);
}
