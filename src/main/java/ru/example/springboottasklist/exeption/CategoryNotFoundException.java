package ru.example.springboottasklist.exeption;

/**
 * Исключение, которое выбрасывается в случае, если категория не найдена
 */
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
