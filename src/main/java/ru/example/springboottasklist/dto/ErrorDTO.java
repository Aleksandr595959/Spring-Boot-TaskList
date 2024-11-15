package ru.example.springboottasklist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @param message Сообщение ошибки
 * @param errors  Список ошибок
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorDTO(
        @Schema(description = "Сообщение ошибки")
        String message,
        @Schema(description = "Список ошибок")
        List<String> errors
) {
    public ErrorDTO(String message, String error) {
        this(message, List.of(error));  // Делегирование каноническому конструктору
    }
}
