package ru.example.springboottasklist.repository.specification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.example.springboottasklist.dto.FilterPageTaskRequestDto;
import ru.example.springboottasklist.entity.Task;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Спецификация для фильтрации и поиска задач, основанная на параметрах,
 * предоставленных в {@link FilterPageTaskRequestDto}.
 * Данный класс реализует интерфейс {@link Specification},
 * который позволяет создавать динамические запросы к базе данных с использованием Java Persistence API (JPA).
 */
@RequiredArgsConstructor
public class TaskSpecification implements Specification<Task> {

    private final FilterPageTaskRequestDto filterPageTaskRequestDto;
    private final Long userId;

    /**
     * Создает предикаты для фильтрации задач на основе предоставленных параметров.
     *
     * @param root            корень запроса, представляющий сущность {@link Task}
     * @param query           объект {@link CriteriaQuery}, представляющий сам запрос
     * @param criteriaBuilder объект {@link CriteriaBuilder}, используемый для создания предикатов
     * @return объединенный предикат для фильтрации задач на основе заданных условий
     */
    @Override
    public Predicate toPredicate(@NonNull Root<Task> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();


        if (filterPageTaskRequestDto.taskTitle() != null) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("taskTitle")),
                    filterPageTaskRequestDto.taskTitle().toUpperCase()));
        }
        if (filterPageTaskRequestDto.categoryTitle() != null) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("category").get("categoryTitle")),
                    filterPageTaskRequestDto.categoryTitle().toUpperCase()));
        }
        if (filterPageTaskRequestDto.status() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), filterPageTaskRequestDto.status()));
        }
        if (filterPageTaskRequestDto.title() != null) {
            predicates.add(criteriaBuilder.equal(root.get("title"), filterPageTaskRequestDto.title()));

        }
        if (filterPageTaskRequestDto.timeFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdTask"),
                    filterPageTaskRequestDto.timeFrom()));
        }
        if (filterPageTaskRequestDto.timeTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdTask"),
                    filterPageTaskRequestDto.timeTo()));
        }
        if (userId != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

