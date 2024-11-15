package ru.example.springboottasklist.exeption;

/**
 * Исключение, которое выбрасывается в случае, если задача не найдена
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
