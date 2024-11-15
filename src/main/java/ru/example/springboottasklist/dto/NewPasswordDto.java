package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @param currentPassword Текущий пароль
 * @param newPassword     Новый пароль
 */
public record NewPasswordDto(
        @NotBlank
        @Size(min = 8, max = 16)
        @Schema(description = "Текущий пароль пользователя", example = "CurrentPassword", minLength = 8, maxLength = 16)
        String currentPassword,
        @NotBlank
        @Size(min = 8, max = 16)
        @Schema(description = "Новый пароль пользователя", example = "NewPassword", minLength = 8, maxLength = 16)
        String newPassword
) {
}
