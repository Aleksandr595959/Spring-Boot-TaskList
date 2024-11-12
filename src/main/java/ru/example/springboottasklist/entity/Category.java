package ru.example.springboottasklist.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(unique = true)
    private String categoryTitle;

//    @Column
//    private Long completedCount;
//
//    @Column
//    private Long uncompletedCount;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
