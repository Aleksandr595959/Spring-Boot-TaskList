package ru.example.springboottasklist.service;

import org.springframework.data.domain.Sort;
import ru.example.springboottasklist.dto.FilterPageTaskRequestDto;
import ru.example.springboottasklist.dto.FilterPageTaskResponseDto;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.dto.TasksDto;

import java.util.List;

public interface TaskService {
    TaskDto addTask(TaskDto taskDto);
    TasksDto getTaskAllUser();
    void removeTask(Long id);
    TaskDto updateTask(Long id,TaskDto taskDto);
    List<FilterPageTaskResponseDto> getTasksUser(FilterPageTaskRequestDto filterPageTaskRequestDto, Sort sort, Integer page, Integer size);
}
