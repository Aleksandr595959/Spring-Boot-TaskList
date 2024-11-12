package ru.example.springboottasklist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.example.springboottasklist.dto.CategoriesDto;
import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateOrUpdateCategoryDto;
import ru.example.springboottasklist.service.CategoryService;

import javax.validation.Valid;

/**
 * Контроллер для работы с категориями задач.
 */
@Slf4j
@RequestMapping("/category")
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Категория")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Создание категории.
     *
     * @param createOrUpdateCategoryDto объект с данными для создания или обновления категории
     * @return {@link ResponseEntity} с объектом {@link CategoryDto}
     */
    @PostMapping("/create")
    @Operation(summary = "Создание категории",
            description = "Создание категории")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateOrUpdateCategoryDto createOrUpdateCategoryDto) {
        return ResponseEntity.ok(categoryService.addCategory(createOrUpdateCategoryDto));
    }

    /**
     * Обновление задачи.
     *
     * @param id                        идентификатор категории
     * @param createOrUpdateCategoryDto объект с данными для создания или обновления категории
     * @return {@link ResponseEntity} с объектом {@link CategoryDto}
     */
    @PatchMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") Long id,
                                                      @Valid @RequestBody CreateOrUpdateCategoryDto createOrUpdateCategoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(createOrUpdateCategoryDto, id));
    }

    /**
     * Удаление задачи.
     *
     * @param id идентификатор категории
     * @return {@link ResponseEntity} без тела (No Content)
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Valid> deleteCategory(@PathVariable("id") Long id) {
        categoryService.removeCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение всех категорий пользователя.
     *
     * @return {@link ResponseEntity} с объектом {@link CategoriesDto}
     */
    @GetMapping()
    public ResponseEntity<CategoriesDto> getAllCategories() {
        CategoriesDto categoriesDto = categoryService.getAllCategories();
        return ResponseEntity.ok(categoriesDto);
    }
}
