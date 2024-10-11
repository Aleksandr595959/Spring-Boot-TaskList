package ru.example.springboottasklist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static ru.example.springboottasklist.utils.DataConstants.DATE_PATTERN;

public record FilterPageTaskResponseDto(
        Long id,
        String taskTitle,
        PriorityTitle title,
        Category category,
        Status status,
        @DateTimeFormat(pattern = DATE_PATTERN)
        @JsonFormat(pattern = DATE_PATTERN)
        LocalDateTime time
) {
}
