package ru.example.springboottasklist.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @param id         id задачи
 * @param taskTitle  Название задачи
 * @param title      Приоритет задачи
 * @param categoryId id категории
 * @param status     Статус выполнения задачи
 */
public record TaskDto(
        @Schema(description = "id задачи")
        Long id,
        @Size(min = 4, max = 32)
        @Schema(description = "Название задачи", minLength = 4, maxLength = 32)
        String taskTitle,
        @Schema(description = "Приоритет задачи")
        PriorityTitle title,
        @Schema(description = "id категории")
        Long categoryId,
        @Schema(description = "Статус выполнения задачи")
        Status status


) {
}
