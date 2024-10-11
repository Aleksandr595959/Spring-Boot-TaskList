package ru.example.springboottasklist.dto;

import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import java.time.LocalDateTime;

import static ru.example.springboottasklist.utils.DataConstants.DATE_PATTERN;

public record FilterPageTaskRequestDto(
        String taskTitle,
        PriorityTitle title,
        Status status,
        Category category,
        @DateTimeFormat(pattern = DATE_PATTERN)
        LocalDateTime timeFrom,

        @DateTimeFormat(pattern = DATE_PATTERN)
        LocalDateTime timeTo
) {
}
