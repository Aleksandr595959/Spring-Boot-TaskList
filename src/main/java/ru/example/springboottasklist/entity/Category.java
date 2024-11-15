package ru.example.springboottasklist.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Класс Category представляет сущность категории, используемую в системе.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    /**
     * Название категории.
     */
    @Column(unique = true)
    private String categoryTitle;

    /**
     * Задачи категории.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Task> tasks;

    /**
     * Пользователь, создавший категорию.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
