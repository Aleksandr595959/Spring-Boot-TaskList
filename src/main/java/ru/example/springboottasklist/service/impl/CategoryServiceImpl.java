package ru.example.springboottasklist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboottasklist.dto.CategoriesDto;
import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateOrUpdateCategoryDto;
import ru.example.springboottasklist.entity.Category;
import ru.example.springboottasklist.entity.User;
import ru.example.springboottasklist.exeption.CategoryNotFoundException;
import ru.example.springboottasklist.mapper.CategoryMapper;
import ru.example.springboottasklist.repository.CategoryRepository;
import ru.example.springboottasklist.service.CategoryService;
import ru.example.springboottasklist.utils.AuthUtils;

import java.util.List;

/**
 * Сервис категорий.
 * Осуществляет операции добавления, обновления, удаления и получения категорий.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final AuthUtils authUtils;

    /**
     * Добавляет новую категорию.
     *
     * @param categoryDto объект с данными для создания или обновления категории
     * @return созданная категория в виде объекта CategoryDto
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Override
    public CategoryDto addCategory(CreateOrUpdateCategoryDto categoryDto) {
        log.info("Was invoked method for : addCategory");
        User user = authUtils.getUserFromAuthentication();
        Category category = categoryMapper.createCategoryDtoToCategory(categoryDto, user);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    /**
     * Обновляет категорию по его идентификатору.
     *
     * @param id          идентификатор категории
     * @param categoryDto объект с данными для создания или обновления категории
     * @return обновленная категория в виде объекта CategoryDto
     * @throws CategoryNotFoundException если категория не найдена
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Override
    public CategoryDto updateCategory(CreateOrUpdateCategoryDto categoryDto, Long id) {
        log.info("Was invoked method for : updateCategory");

        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Отсутствует категория по данному id" + id));
        Category updatedCategory = categoryMapper.updateCategoryDtoToCategory(category, categoryDto);
        categoryRepository.save(updatedCategory); //можно попробовать без этого
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    /**
     * Удаляет категорию по её идентификатору.
     *
     * @param id идентификатор категории
     */
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @categoryRepository.getById(#id).getUser().login")
    @Override
    public void removeCategory(Long id) {
        log.info("Was invoked method for : removeCategory");

        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Отсутствует категория по данному id" + id));
        categoryRepository.delete(category);
    }

    /**
     * Возвращает все категории, принадлежащие авторизованному пользователю.
     *
     * @return объект CategoriesDto со списком объявлений авторизованного пользователя
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Override
    public CategoriesDto getAllCategories() {
        log.info("Was invoked method for : getAllCategories");

        User user = authUtils.getUserFromAuthentication();
        List<CategoryDto> categoriesList = user.getCategories().stream()
//                .map(Task::getCategory)
//                .filter(Objects::nonNull)
////               .distinct()
                .map(categoryMapper::toCategoryDto)
                .toList();
//        List<Category> categories = user.getCategories();
//        List<CategoryDto> categoriesList = categoryMapper.toCategoryDto(categories);
        return new CategoriesDto(categoriesList);
    }

    /**
     * Возвращает расширенную информацию по категории по её идентификатору.
     *
     * @param id идентификатор объявления
     */
    @Override
    public Category getCategoryById(final Long id) {
        log.info("Was invoked method for : getCategoryById");

        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Отсутствует категория по данному id: " + id));
    }
}
