package ru.example.springboottasklist.exeption;

public class TaskCreateException extends RuntimeException{
    public TaskCreateException(String message) {
        super(message);
    }
}
