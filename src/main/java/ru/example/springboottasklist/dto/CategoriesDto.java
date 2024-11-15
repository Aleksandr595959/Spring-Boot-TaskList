package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @param categories Список категорий пользователя
 */
public record CategoriesDto(
        @NotEmpty
        @Schema(description = "Список категорий пользователя")
        List<CategoryDto> categories
) {
}
