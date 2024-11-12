package ru.example.springboottasklist.service;


import ru.example.springboottasklist.dto.CategoriesDto;
import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateOrUpdateCategoryDto;
import ru.example.springboottasklist.entity.Category;


public interface CategoryService {
    CategoryDto addCategory(CreateOrUpdateCategoryDto categoryDto);

    CategoryDto updateCategory(CreateOrUpdateCategoryDto categoryDto, Long id);

    void removeCategory(Long id);

    CategoriesDto getAllCategories();

    Category getCategoryById(Long id);
}
