package ru.example.springboottasklist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateOrUpdateCategoryDto;
import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.entity.User;

@Mapper
public interface CategoryMapper {

    Category toEntity(CreateOrUpdateCategoryDto categoryDto);

    CategoryDto toDto(Category category);

    Category updateCategoryDtoToCategory(@MappingTarget Category category, CreateOrUpdateCategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Category createCategoryDtoToCategory(CreateOrUpdateCategoryDto categoryDto, User user);
}
