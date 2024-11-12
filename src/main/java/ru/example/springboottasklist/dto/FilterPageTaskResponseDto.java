package ru.example.springboottasklist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import java.time.LocalDateTime;

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
