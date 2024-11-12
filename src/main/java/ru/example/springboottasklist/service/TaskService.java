package ru.example.springboottasklist.service;

import org.springframework.data.domain.Sort;
import ru.example.springboottasklist.dto.*;

import java.util.List;

public interface TaskService {
    TaskDto addTask(CreateOrUpdateTaskDto taskDto);
    TasksDto getAllTasksByUser();
    void removeTask(Long id);
    TaskDto updateTask(Long id,CreateOrUpdateTaskDto taskDto);
    List<FilterPageTaskResponseDto> getTasksUser(FilterPageTaskRequestDto filterPageTaskRequestDto, Sort sort, Integer page, Integer size);
}
