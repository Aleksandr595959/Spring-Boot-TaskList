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

@RequiredArgsConstructor
public class TaskSpecification implements Specification<Task> {
    private final FilterPageTaskRequestDto filterPageTaskRequestDto;
    private final Long userId;
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
        // Фильтрация по пользователю
//        if (filterPageTaskRequestDto.userId() != null) {
//            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), filterPageTaskRequestDto.userId()));
//        }
        // Фильтрация по userId, если он не null (не администратор)
        if (userId != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

