package ru.example.springboottasklist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @param message Сообщение ошибки
 * @param errors  Список ошибок
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorDTO(
        String message,
        List<String> errors
) {
    public ErrorDTO(String message, String error) {
        this(message, List.of(error));  // Делегирование каноническому конструктору
    }
}
