package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @param count Количество задач
 * @param tasks Список задач
 */
public record TasksDto(
        @Schema(description = "количество задач")
        Integer count,
        @Schema(description = "Список задач")
        List<TaskDto> tasks
) {
}
