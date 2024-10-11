package ru.example.springboottasklist.service;


import ru.example.springboottasklist.dto.CategoriesDto;
import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateCategoryDto;
import ru.example.springboottasklist.entity.Category;

import java.util.List;


public interface CategoryService {
    CategoryDto addCategory(CreateCategoryDto categoryDto);

    CategoryDto updateCategory(CreateCategoryDto categoryDto, Long id);

    void removeCategory(Long id);

    CategoriesDto getAllCategories();

    Category getCategoryById(Long id);
}
