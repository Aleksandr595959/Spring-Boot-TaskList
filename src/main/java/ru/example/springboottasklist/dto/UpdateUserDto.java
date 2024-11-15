package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @param firstName Имя пользователя
 * @param lastName  Фамилия пользователя
 * @param phone     Номер контактного телефона
 */
public record UpdateUserDto(
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
        String phone
) {
}
