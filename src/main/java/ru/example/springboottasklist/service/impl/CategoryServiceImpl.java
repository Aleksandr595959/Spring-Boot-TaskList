package ru.example.springboottasklist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboottasklist.dto.CategoriesDto;
import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateCategoryDto;
import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.entity.Task;
import ru.example.springboottasklist.entity.User;
import ru.example.springboottasklist.exeption.CategoryNotFoundException;
import ru.example.springboottasklist.mapper.CategoryMapper;
import ru.example.springboottasklist.repository.CategoryRepository;
import ru.example.springboottasklist.service.CategoryService;
import ru.example.springboottasklist.utils.AuthUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final AuthUtils authUtils;

    @Override
    public CategoryDto addCategory(CreateCategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }


    @Override
    public CategoryDto updateCategory(CreateCategoryDto categoryDto, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        Category updatedCategory = categoryMapper.updateCategoryDtoToCategory(category, categoryDto);
        categoryRepository.save(updatedCategory);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public void removeCategory(Long id) {
    Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    categoryRepository.delete(category);
    }

    @Override
    public CategoriesDto getAllCategories() {
        User user = authUtils.getUserFromAuthentication();
        List<CategoryDto> categoriesList = user.getTasks().stream()
                .map(Task::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .map(categoryMapper::toCategoryDto)
                .toList();
        return new CategoriesDto(categoriesList);
    }

    @Override
    public Category getCategoryById(final Long id) {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }
}
