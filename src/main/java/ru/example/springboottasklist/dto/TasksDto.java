package ru.example.springboottasklist.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @param count Oбщее количество задач
 * @param tasks Список задач
 */
public record TasksDto(
        Integer count,
        @NotEmpty
        List<TaskDto> tasks
) {
}
