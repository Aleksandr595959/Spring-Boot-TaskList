package ru.example.springboottasklist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import java.time.LocalDateTime;

/**
 * @param id          id задачи
 * @param taskTitle   Название задачи
 * @param title       Приоритет задачи
 * @param status      Статус выполнения задачи
 * @param categoryId  id категории
 * @param createdTask Время создания задачи
 */
public record FilterPageTaskResponseDto(
        Long id,
        String taskTitle,
        PriorityTitle title,
        Long categoryId,
        Status status,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdTask
) {
}
