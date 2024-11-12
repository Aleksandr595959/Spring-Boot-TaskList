package ru.example.springboottasklist.exeption.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.example.springboottasklist.dto.ErrorDTO;
import ru.example.springboottasklist.exeption.CategoryNotFoundException;
import ru.example.springboottasklist.exeption.TaskNotFoundException;

import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass extends ResponseEntityExceptionHandler {
    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        var errors = new ArrayList<String>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(objectError ->
                errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage()));

        var apiError = new ErrorDTO("Аргумент(ы) не прошли валидацию", errors);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(TaskNotFoundException e) {
        return getErrorDTOResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(CategoryNotFoundException e) {
        return getErrorDTOResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }


    private ResponseEntity<ErrorDTO> getErrorDTOResponseEntity(HttpStatus httpStatus, String message) {
        var response = new ErrorDTO(message, httpStatus.value() + "");
        return new ResponseEntity<>(response, httpStatus);
    }
}