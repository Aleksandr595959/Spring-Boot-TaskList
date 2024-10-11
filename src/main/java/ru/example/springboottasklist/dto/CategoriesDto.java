package ru.example.springboottasklist.dto;

import ru.example.springboottasklist.entity.Category;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public record CategoriesDto(
        @NotEmpty
        List<CategoryDto> categories
) {
}
