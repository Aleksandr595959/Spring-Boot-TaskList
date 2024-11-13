package ru.example.springboottasklist.dto;

import org.springframework.format.annotation.DateTimeFormat;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

/**
 * @param taskTitle     Название задачи
 * @param title         Приоритет задачи
 * @param status        Статус выполнения задачи
 * @param categoryTitle Название категории задачи
 * @param timeFrom      Начальное время поиска
 * @param timeTo        Конечное время поиска
 */
public record FilterPageTaskRequestDto(
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
