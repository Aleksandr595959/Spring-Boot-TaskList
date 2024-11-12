package ru.example.springboottasklist.mapper;

import org.mapstruct.*;
import ru.example.springboottasklist.dto.CreateOrUpdateTaskDto;
import ru.example.springboottasklist.dto.FilterPageTaskResponseDto;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.entity.Task;
import ru.example.springboottasklist.entity.User;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    @Mapping(target = "categoryId", source = "task.category.id")
    TaskDto toDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Task createTaskDtoToTask(CreateOrUpdateTaskDto taskDto, User user);

    List<TaskDto> toTaskDto(List<Task> tasks);

    Task updateTaskDtoToTask(@MappingTarget Task task, CreateOrUpdateTaskDto taskDto);

    @Mapping(target = "categoryId", source = "task.category.id")
    FilterPageTaskResponseDto toStatisticDto(Task task);
}
