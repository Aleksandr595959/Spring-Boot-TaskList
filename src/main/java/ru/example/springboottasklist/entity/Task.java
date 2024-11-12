package ru.example.springboottasklist.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tasks")
public class Task extends BaseEntity {

    @Column
    private String taskTitle;

    @Column
    private Integer completed;

    @CreationTimestamp
    @Column(name = "created_task", nullable = false, updatable = false)
    private LocalDateTime createdTask;

    @Column
    @Enumerated(EnumType.STRING)
    private PriorityTitle title;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
