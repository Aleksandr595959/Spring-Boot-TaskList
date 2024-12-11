package ru.example.springboottasklist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import java.time.LocalDateTime;

/**
 * @param id          id задачи
 * @param taskTitle   Название задачи
 * @param title       Приоритет задачи
 * @param status      Статус выполнения задачи
 * @param categoryId  id категории задачи
 * @param createdTask Время создания задачи
 */
public record FilterPageTaskResponseDto(
        @Schema(description = "id задачи")
        Long id,
        @Schema(description = "Название задачи")
        String taskTitle,
        @Schema(description = "Приоритет задачи")
        PriorityTitle title,
        @Schema(description = "id категории задачи")
        Long categoryId,
        @Schema(description = "Статус выполнения задачи")
        Status status,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        @Schema(description = "Время создания задачи")
        String createdTask
) {
}
