package ru.example.springboottasklist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.example.springboottasklist.dto.*;
import ru.example.springboottasklist.service.TaskService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для работы с задачами.
 */
@Slf4j
@RequestMapping("/task")
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Задачи")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    /**
     * Добавление задачи.
     *
     * @param createOrUpdateTaskDto объект с данными для создания или обновления задачи
     * @return {@link ResponseEntity} с объектом {@link TaskDto}
     */
    @PostMapping("/createTask")
    @Operation(summary = "Создание задачи")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateOrUpdateTaskDto createOrUpdateTaskDto) {
        return ResponseEntity.ok(taskService.addTask(createOrUpdateTaskDto));
    }

    /**
     * Получение всех задач пользователя.
     *
     * @return {@link ResponseEntity} с объектом {@link TasksDto}
     */
    @GetMapping("/getAllTaskUser")
    @Operation(summary = "Получение всех задач авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<TasksDto> getAllTaskUser() {
        TasksDto tasksDto = taskService.getAllTasksByUser();
        return ResponseEntity.ok(tasksDto);
    }

    /**
     * Удаление задачи.
     *
     * @param id идентификатор задачи
     * @return {@link ResponseEntity} без тела (No Content)
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Удаление задачи",
            description = "Удаление задачи по идентификационному номеру авторизованным пользователем")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<Valid> removeTask(@PathVariable("id") Long id) {
        taskService.removeTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновление задачи.
     *
     * @param id                    идентификатор задачи
     * @param createOrUpdateTaskDto объект с данными для создания или обновления задачи
     * @return {@link ResponseEntity} с объектом {@link TaskDto}
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление задачи",
            description = "Обновление задачи по id задачи")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Long id,
                                              @Valid @RequestBody CreateOrUpdateTaskDto createOrUpdateTaskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, createOrUpdateTaskDto));
    }

    /**
     * Фильтрация и пагинация задач.
     *
     * @param pageNumber               начальный элемент
     * @param pageSize                 количество элементов
     * @param filterPageTaskRequestDto объект с данными для фильтрации
     * @return {@link ResponseEntity} с объектом {@link FilterPageTaskResponseDto}
     */
    @GetMapping("getTaskUser")
    @Operation(summary = "Фильтрация задач",
            description = "Фильтрация и пагинация задач пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<List<FilterPageTaskResponseDto>> getTaskUser(@RequestParam("page") Integer pageNumber,
                                                                       @RequestParam("size") Integer pageSize,
                                                                       @Valid @ModelAttribute FilterPageTaskRequestDto filterPageTaskRequestDto,
                                                                       @SortDefault(sort = "id") Sort sort) {
        return ResponseEntity.ok(taskService.getTasksUser(filterPageTaskRequestDto, sort, pageNumber, pageSize));
    }

}
