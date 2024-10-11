package ru.example.springboottasklist.dto;

import ru.example.springboottasklist.enums.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record UserDto(
        Long id,
        @NotBlank(message = "Поле логин не может быть пустым")
        @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
        @Email
        String username,
        @NotBlank(message = "Поле пароль не может быть пустым")
        @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
        String password,
        @NotBlank(message = "Имя автора не может быть пустым")
        @Size(min = 2, max = 16, message = "Имя автора должно быть от 2 до 16 символов")
        String firstName,
        @NotBlank(message = "Фамилия автора не может быть пустым")
        @Size(min = 2, max = 16, message = "Фамилия автора должно быть от 2 до 16 символов")
        String lastName,
        @NotBlank(message = "Введите номер Вашего контактного телефона")
        @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
                message = "Введите номер в формате +7 (000) 000-00-00")
        String phone,
        Role role
) {
}
