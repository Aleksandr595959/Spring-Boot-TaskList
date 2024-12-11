package ru.example.springboottasklist.mapper;

import org.mapstruct.*;
import ru.example.springboottasklist.dto.CreateOrUpdateTaskDto;
import ru.example.springboottasklist.dto.FilterPageTaskResponseDto;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.entity.Task;
import ru.example.springboottasklist.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    /**
     * Преобразует объект `Task` в объект `TaskDto`, расширенную версию DTO для объявления.
     *
     * @param task объект `Task` для преобразования
     * @return объект `TaskDto`
     */
    @Mapping(target = "categoryId", source = "task.category.id")
    TaskDto toDto(Task task);

    /**
     * Создает объект `Task` на основе данных из `CreateOrUpdateTaskDto` и `User`.
     *
     * @param taskDto данные для создания или обновления задачи
     * @param user    пользователь, создающий или обновляющий задачу
     * @return объект `Task`
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdTask", expression = "java(java.time.LocalDateTime.now())")
    Task createTaskDtoToTask(CreateOrUpdateTaskDto taskDto, User user);
    /**
     * Преобразует список объектов `Task` в список объектов `TaskDto`.
     *
     * @param tasks список задач
     * @return список `TaskDto`
     */
    List<TaskDto> toTaskDto(List<Task> tasks);

    /**
     * Обновляет объект `Task`, перекладывая данные из полей `CreateOrUpdateTaskDto`.
     *
     * @param taskDto данные для обновления задачи
     * @param task    существующий объект `Task`
     */
    Task updateTaskDtoToTask(@MappingTarget Task task, CreateOrUpdateTaskDto taskDto);

    /**
     * Преобразует сущность задачи в объект DTO для статистики задач.
     *
     * @param task сущность задачи для преобразования
     * @return объект DTO для статистики задач
     */
    @Mapping(target = "categoryId", source = "task.category.id")
    FilterPageTaskResponseDto toStatisticDto(Task task);
}
