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

    /**
     * Преобразует объект `Category` в объект `CategoryDto`.
     *
     * @param category объект `Category` для преобразования
     * @return объект `CategoryDto`
     */
    CategoryDto toDto(Category category);

    /**
     * Обновляет объект `Category`, перекладывая данные из полей `CreateOrUpdateCategoryDto `.
     *
     * @param categoryDto данные для обновления категории
     * @param category    существующий объект `Category`
     */
    Category updateCategoryDtoToCategory(@MappingTarget Category category, CreateOrUpdateCategoryDto categoryDto);

    /**
     * Преобразует объект `Category` в объект `CategoryDto`, простую версию DTO для категорий.
     *
     * @param category объект `Category` для преобразования
     * @return объект `CategoryDto`
     */
    CategoryDto toCategoryDto(Category category);

    /**
     * Создает объект `Category` на основе данных из `CreateOrUpdateCategoryDto` и `User`.
     *
     * @param categoryDto данные для создания или обновления категории
     * @param user        пользователь, создающий или обновляющий категорию
     * @return объект `Category`
     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Category createCategoryDtoToCategory(CreateOrUpdateCategoryDto categoryDto, User user);
}
