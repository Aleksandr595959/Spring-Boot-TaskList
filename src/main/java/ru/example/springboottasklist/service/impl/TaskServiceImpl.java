package ru.example.springboottasklist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboottasklist.dto.FilterPageTaskRequestDto;
import ru.example.springboottasklist.dto.FilterPageTaskResponseDto;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.dto.TasksDto;
import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.entity.Task;
import ru.example.springboottasklist.entity.User;
import ru.example.springboottasklist.exeption.TaskNotFoundException;
import ru.example.springboottasklist.mapper.TaskMapper;
import ru.example.springboottasklist.repository.TaskRepository;
import ru.example.springboottasklist.repository.specification.TaskSpecification;
import ru.example.springboottasklist.service.CategoryService;
import ru.example.springboottasklist.service.TaskService;
import ru.example.springboottasklist.utils.AuthUtils;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final AuthUtils authUtils;
    private final TaskRepository taskRepository;
    private final CategoryService categoryService;

    @Override
    public TaskDto addTask(TaskDto taskDto) {
        log.info("Was invoked method for : addTask");

        User user = authUtils.getUserFromAuthentication();
        Task task = taskMapper.createTaskDtoToTask(taskDto, user);
        if (taskDto.categoryId() != null) {
            Category category = categoryService.getCategoryById(taskDto.categoryId());
            task.setCategory(category);
        }
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TasksDto getTaskAllUser() {
        log.info("Was invoked method for : getTaskAllUser");
        User user = authUtils.getUserFromAuthentication();
        List<Task> list = user.getTasks();
        List<TaskDto> result = taskMapper.toTaskDto(list);
        return new TasksDto(result.size(), result);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @taskRepository.getById(#id).getUser().login")
    public void removeTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        taskRepository.delete(task);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @taskRepository.getById(#id).getUser().login")
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        Task updateTask = taskMapper.updateTaskDtoToTask(task, taskDto);
        taskRepository.save(updateTask);
        return taskMapper.toDto(updateTask);
    }

    @Override
    public List<FilterPageTaskResponseDto> getTasksUser(final FilterPageTaskRequestDto filterPageTaskRequestDto, final Sort sort, final Integer page, final Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return taskRepository.findAll(new TaskSpecification(filterPageTaskRequestDto), pageable).stream().map(taskMapper::toStatisticDto).toList();
    }
}
