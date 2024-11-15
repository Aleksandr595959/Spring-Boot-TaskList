package ru.example.springboottasklist.exeption.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.example.springboottasklist.dto.ErrorDTO;
import ru.example.springboottasklist.exeption.CategoryNotFoundException;
import ru.example.springboottasklist.exeption.TaskNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

/**
 * Хэндлер для обработки исключений и возвращения соответствующих HTTP-ответов и сообщений.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass extends ResponseEntityExceptionHandler {

    /**
     * Обработчик для исключения {@link MethodArgumentNotValidException}.
     *
     * @param ex      исключение, содержащее информацию о валидации
     * @param headers заголовки HTTP
     * @param status  статус HTTP
     * @param request объект запроса
     * @return ответ с ошибкой валидации
     */
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

    /**
     * Обработчик исключения {@link TaskNotFoundException}.
     *
     * @param e объект текущего исключения.
     * @return ответ с информацией об ошибке.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(TaskNotFoundException e) {
        return getErrorDTOResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Обработчик исключения {@link CategoryNotFoundException}.
     *
     * @param e объект текущего исключения.
     * @return ответ с информацией об ошибке.
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(CategoryNotFoundException e) {
        return getErrorDTOResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Обрабатывает EntityNotFoundException.
     *
     * @param e объект текущего исключения.
     * @return 404 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return getErrorDTOResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Обрабатывает AccessDeniedException.
     *
     * @param e объект текущего исключения.
     * @return 403 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> accessDeniedExceptionHandler(AccessDeniedException e) {
        return getErrorDTOResponseEntity(HttpStatus.FORBIDDEN, e.getMessage());
    }

    /**
     * Обрабатывает исключения, наследуемые от Exception, если они не были обработаны в предыдущих методах.
     *
     * @param e объект текущего исключения.
     * @return 400 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        return getErrorDTOResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Создает ResponseEntity с объектом {@link ErrorDTO}.
     *
     * @param httpStatus статус HTTP для ответа
     * @param message    сообщение об ошибке
     * @return ResponseEntity с информацией об ошибке
     */
    private ResponseEntity<ErrorDTO> getErrorDTOResponseEntity(HttpStatus httpStatus, String message) {
        var response = new ErrorDTO(message, httpStatus.value() + "");
        return new ResponseEntity<>(response, httpStatus);
    }
}