package ru.example.springboottasklist.dto;

import javax.validation.constraints.Size;

public record CreateCategoryDto(
        @Size(min = 4, max = 32, message = "Заголовок категории должен содержать не менее 4 и не более 32 символов")
        String categoryTitle) {
}
