package ru.example.springboottasklist.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public record TasksDto(
     Integer count,
     @NotEmpty
     List<TaskDto> tasks
) {
}
