package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import java.time.LocalDateTime;

/**
 * @param taskTitle     Название задачи
 * @param title         Приоритет задачи
 * @param status        Статус выполнения задачи
 * @param categoryTitle Название категории задачи
 * @param timeFrom      Начальное время поиска
 * @param timeTo        Конечное время поиска
 */
public record FilterPageTaskRequestDto(
        @Schema(description = "Название задачи")
        String taskTitle,
        @Schema(description = "Приоритет задачи")
        PriorityTitle title,
        @Schema(description = "Статус выполнения задачи")
        Status status,
        @Schema(description = "Название категории задачи")
        String categoryTitle,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        @Schema(description = "Начальное время поиска")
        LocalDateTime timeFrom,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        @Schema(description = "Конечное время поиска")
        LocalDateTime timeTo
) {
}
