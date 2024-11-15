package ru.example.springboottasklist.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Базовый класс для сущностей.
 */
@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    /**
     * Идентификатор сущности.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
}
