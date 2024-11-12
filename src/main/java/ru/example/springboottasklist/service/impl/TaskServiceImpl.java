package ru.example.springboottasklist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboottasklist.dto.*;
import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.entity.Task;
import ru.example.springboottasklist.entity.User;
import ru.example.springboottasklist.enums.Role;
import ru.example.springboottasklist.exeption.TaskNotFoundException;
import ru.example.springboottasklist.mapper.TaskMapper;
import ru.example.springboottasklist.repository.TaskRepository;
import ru.example.springboottasklist.repository.specification.TaskSpecification;
import ru.example.springboottasklist.service.CategoryService;
import ru.example.springboottasklist.service.TaskService;
import ru.example.springboottasklist.utils.AuthUtils;

import java.util.List;

/**
 * Сервис задач.
 * Осуществляет операции добавления, обновления, удаления и получения задач.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final AuthUtils authUtils;
    private final TaskRepository taskRepository;
    private final CategoryService categoryService;

    /**
     * Добавляет новую задачу.
     *
     * @param createOrUpdateTaskDto объект с данными для создания или обновления pflfxb
     * @return созданное объявление в виде объекта TaskDto
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public TaskDto addTask(CreateOrUpdateTaskDto createOrUpdateTaskDto) {
        log.info("Was invoked method for : addTask");

        User user = authUtils.getUserFromAuthentication();
        Task task = taskMapper.createTaskDtoToTask(createOrUpdateTaskDto, user);
        if (createOrUpdateTaskDto.categoryId() != null) {
            Category category = categoryService.getCategoryById(createOrUpdateTaskDto.categoryId());
            task.setCategory(category);
        }
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    /**
     * Возвращает все задачи текущего пользователя.
     *
     * @return объект TasksDto с количеством и списком всех задач
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Override
    public TasksDto getAllTasksByUser() {
        log.info("Was invoked method for : getTaskAllUser");

        User user = authUtils.getUserFromAuthentication();
        List<Task> list = user.getTasks();
        List<TaskDto> result = taskMapper.toTaskDto(list);
        return new TasksDto(result.size(), result);
    }

    /**
     * Удаляет задачу по её идентификатору.
     *
     * @param id идентификатор задачи
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @taskRepository.getById(#id).getUser().login")
    public void removeTask(Long id) {
        log.info("Was invoked method for : removeTask");

        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Нет задачи с id : " + id));
        taskRepository.delete(task);
    }

    /**
     * Обновляет задачу по её идентификатору.
     *
     * @param id                    идентификатор задачи
     * @param createOrUpdateTaskDto объект с данными для создания или обновления задачи
     * @return обновленную задачу в виде объекта TaskDto
     * @throws TaskNotFoundException если задача не найдена
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @taskRepository.getById(#id).getUser().login")
    public TaskDto updateTask(Long id, CreateOrUpdateTaskDto createOrUpdateTaskDto) throws TaskNotFoundException {
        log.info("Was invoked method for : updateTask");

        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Нет задачи с id : " + id));
        Task updateTask = taskMapper.updateTaskDtoToTask(task, createOrUpdateTaskDto);
        taskRepository.save(updateTask); //попробовать без этого
        return taskMapper.toDto(updateTask);
    }

    /**
     * Фильтрация и пагинация задачи.
     *
     * @param page                     с какого элемента
     * @param size                     по какой элемент
     * @param filterPageTaskRequestDto объект с данными для фильтрации
     * @return список задач по фильтру в виде списка объектов FilterPageTaskResponseDto
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Override
    public List<FilterPageTaskResponseDto> getTasksUser(final FilterPageTaskRequestDto filterPageTaskRequestDto, final Sort sort, final Integer page, final Integer size) {
        log.info("Was invoked method for : getTasksUser");

        User user = authUtils.getUserFromAuthentication();
        Long userId = user.getRole() == Role.ADMIN ? null : user.getId();

        Pageable pageable = PageRequest.of(page, size, sort);
        TaskSpecification spec = new TaskSpecification(filterPageTaskRequestDto, userId);

        var result = taskRepository.findAll(spec, pageable).stream()
                .map(taskMapper::toStatisticDto)
                .toList();

        return result;
    }
}
