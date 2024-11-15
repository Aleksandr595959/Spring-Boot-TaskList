package ru.example.springboottasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;

/**
 * @param id            id категории
 * @param categoryTitle Название категории
 */
public record CategoryDto(
        @Schema(description = "id категории")
        Long id,
        @Size(min = 4, max = 32)
        @Schema(description = "Название категории",minLength =4, maxLength = 32)
        String categoryTitle) {
}
