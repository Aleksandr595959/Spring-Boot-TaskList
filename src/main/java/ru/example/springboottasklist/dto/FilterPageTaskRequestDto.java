package ru.example.springboottasklist.dto;

import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public record FilterPageTaskRequestDto(
        Long userId,
        String taskTitle,
        PriorityTitle title,
        @Enumerated(EnumType.STRING)
        Status status,
        String categoryTitle,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timeFrom,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timeTo
) {
}
