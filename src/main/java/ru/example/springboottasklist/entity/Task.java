package ru.example.springboottasklist.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, представляющий задачу.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tasks")
public class Task extends BaseEntity {

    /**
     * Название задачи.
     */
    @Column
    private String taskTitle;

    /**
     * Время создания объявления.
     */
//    @CreationTimestamp
    @Column(name = "created_task", updatable = false)
    private LocalDateTime createdTask;

    /**
     * Приоритет задачи.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private PriorityTitle title;

    /**
     * Статус выполнения задачи.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Категория задачи.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Пользователь, создавший задачу.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
