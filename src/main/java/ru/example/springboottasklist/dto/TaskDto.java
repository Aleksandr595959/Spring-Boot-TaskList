package ru.example.springboottasklist.dto;


import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import javax.validation.constraints.*;

public record TaskDto(
        @NotBlank(message = "Заголовок задачи не может быть пустым")
        @Size(min = 4, max = 32, message = "Заголовок задачи должен содержать не менее 4 и не более 32 символов")
        String taskTitle,
        PriorityTitle title,
        Long categoryId,
        Status status





) {
}
