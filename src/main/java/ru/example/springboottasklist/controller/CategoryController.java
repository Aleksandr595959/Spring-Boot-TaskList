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
import ru.example.springboottasklist.dto.CreateCategoryDto;
import ru.example.springboottasklist.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/category")
@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Категория")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "Создание категории",
            description = "Создание категории")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.addCategory(categoryDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") Long id,
                                                      @Valid @RequestBody CreateCategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, id));
    }

    @DeleteMapping("/id")
    public ResponseEntity<Valid> deleteCategory(@PathVariable("id") Long id) {
        categoryService.removeCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<CategoriesDto> getAllCategories() {
        CategoriesDto categoriesDto = categoryService.getAllCategories();
        return ResponseEntity.ok(categoriesDto);
    }
}
