package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @param username Логин
 * @param password Пароль
 */
public record LoginDto(
        @NotBlank
        @Size(min = 4, max = 32)
        @Email
        @Schema(description = "Логин пользователя" , example = "john.doe@example.com", minLength = 4, maxLength = 32)
        String username,
        @NotBlank
        @Size(min = 8, max = 16)
        @Schema(description = "Пароль пользователя", example = "password123", minLength = 8, maxLength = 16)
        String password
) {
}
