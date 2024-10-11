package ru.example.springboottasklist.mapper;

import org.mapstruct.*;
import ru.example.springboottasklist.dto.FilterPageTaskResponseDto;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.entity.Task;
import ru.example.springboottasklist.entity.User;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Task createTaskDtoToTask(TaskDto taskDto, User user);

    List<TaskDto> toTaskDto(List<Task> tasks);

    Task updateTaskDtoToTask(@MappingTarget Task task, TaskDto taskDto);

    FilterPageTaskResponseDto toStatisticDto(Task task);
}
