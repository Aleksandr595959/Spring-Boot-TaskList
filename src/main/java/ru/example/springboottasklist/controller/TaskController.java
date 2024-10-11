package ru.example.springboottasklist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.example.springboottasklist.dto.FilterPageTaskRequestDto;
import ru.example.springboottasklist.dto.FilterPageTaskResponseDto;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.dto.TasksDto;
import ru.example.springboottasklist.service.TaskService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/task")
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Задачи")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/creteTask")
    @Operation(summary = "Создание задачи")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.addTask(taskDto));
    }

    @GetMapping("/getAllTaskUser")
    @Operation(summary = "Получение всех задач пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<TasksDto> getAllTaskUser() {
        TasksDto tasksDto = taskService.getTaskAllUser();
        return ResponseEntity.ok(tasksDto);
    }

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

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление задачи",
            description = "Обновление задачи по id задачи")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Long id,
                                              @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }

    @GetMapping("getTaskUser")
    public ResponseEntity<List<FilterPageTaskResponseDto>> getTaskUser(@RequestParam("page") Integer pageNumber,
                                                                       @RequestParam("size") Integer pageSize,
                                                                       @Valid @ModelAttribute FilterPageTaskRequestDto dto, Sort sort) {
        return ResponseEntity.ok(taskService.getTasksUser(dto, sort, pageNumber, pageSize));
    }
}
