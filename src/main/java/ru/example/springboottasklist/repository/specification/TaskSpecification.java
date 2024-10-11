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

    @Override
    public Predicate toPredicate(@NonNull Root<Task> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (filterPageTaskRequestDto.taskTitle() != null) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("taskTitle")),
                    filterPageTaskRequestDto.taskTitle().toUpperCase()));
        }
        if (filterPageTaskRequestDto.category() != null) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("category")),
                    filterPageTaskRequestDto.category()));
        }
        if (filterPageTaskRequestDto.status() != null) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("status")),
                    filterPageTaskRequestDto.status().name()));
        }
        if (filterPageTaskRequestDto.title() != null) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("title")),
                    filterPageTaskRequestDto.title().name()));
        }
        if (filterPageTaskRequestDto.timeFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time"),
                    filterPageTaskRequestDto.timeFrom()));
        }
        if (filterPageTaskRequestDto.timeTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("time"),
                    filterPageTaskRequestDto.timeTo()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

