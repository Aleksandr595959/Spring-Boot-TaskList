package ru.example.springboottasklist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.example.springboottasklist.dto.RegisterDto;
import ru.example.springboottasklist.dto.UpdateUserDto;
import ru.example.springboottasklist.dto.UserDto;
import ru.example.springboottasklist.entity.User;

@Mapper
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder encoder;

    @Mapping(target = "login", source = "username")
    @Mapping(target = "password", expression = "java(encryptPassword(registerDto))")
    public abstract User toEntity(RegisterDto registerDto);

    public abstract User toEntity(User user);

    @Mapping(target = "username", source = "login")
    public abstract UserDto toDto(User user);

    public abstract UpdateUserDto updateUserDtoFromUser(User user);

    public abstract void updateUserFromDto(@MappingTarget User user, UpdateUserDto updateUser);

    protected String encryptPassword(RegisterDto registerDto) {
        return encoder.encode(registerDto.password());
    }

}
