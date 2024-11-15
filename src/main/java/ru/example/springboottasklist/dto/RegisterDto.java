package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.example.springboottasklist.enums.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @param username  Логин
 * @param password  Пароль
 * @param firstName Имя пользователя
 * @param lastName  Фамилия пользователя
 * @param phone     Номер контактного телефона
 * @param role      Роль пользователя
 */
public record RegisterDto(
        @NotBlank
        @Size(min = 4, max = 32)
        @Email
        @Schema(description = "Логин пользователя" , example = "john.doe@example.com", minLength = 4, maxLength = 32)
        String username,
        @NotBlank
        @Size(min = 8, max = 16)
        @Schema(description = "Пароль пользователя", example = "password123", minLength = 8, maxLength = 16)
        String password,
        @NotBlank
        @Size(min = 2, max = 16)
        @Schema(description = "Имя пользователя", example = "Bob", minLength = 2, maxLength = 16)
        String firstName,
        @NotBlank
        @Size(min = 2, max = 16)
        @Schema(description = "Фамилия пользователя", example = "Jonson", minLength = 2, maxLength = 16)
        String lastName,
        @NotBlank
        @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
        @Schema(description = "Номер телефона пользователя", example = "+7 (000) 000-00-00")
        String phone,
        @NotBlank
        @Schema(description = "Роль пользователя")
        Role role
) {
}
