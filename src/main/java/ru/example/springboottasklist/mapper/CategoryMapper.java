package ru.example.springboottasklist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import ru.example.springboottasklist.dto.CategoriesDto;
import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateCategoryDto;
import ru.example.springboottasklist.entity.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Category toEntity(CreateCategoryDto categoryDto);

    CategoryDto toDto(Category category);

    Category updateCategoryDtoToCategory(@MappingTarget Category category, CreateCategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

}
