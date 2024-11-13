package ru.example.springboottasklist.dto;


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
        Long id,
        @NotBlank(message = "Заголовок задачи не может быть пустым")
        @Size(min = 4, max = 32, message = "Заголовок задачи должен содержать не менее 4 и не более 32 символов")
        String taskTitle,
        PriorityTitle title,
        Long categoryId,
        Status status


) {
}
