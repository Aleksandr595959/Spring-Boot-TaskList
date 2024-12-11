package ru.example.springboottasklist.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.example.springboottasklist.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, представляющий пользователя.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    /**
     * Логин пользователя.
     */
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * Пароль пользователя.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Имя пользователя.
     */
    @Column
    private String lastName;

    /**
     * Фамилия пользователя.
     */
    @Column
    private String firstName;

    /**
     * Телефон пользователя.
     */
    @Column
    private String phone;

    /**
     * Роль пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Время создания учетной записи пользователя.
     */

    @Column(name = "date_of_created", updatable = false)
    private LocalDateTime dateOfCreated;

    /**
     * Задачи пользователя.
     */
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    /**
     * Категории пользователя.
     */
    @OneToMany(mappedBy = "user")
    private List<Category> categories;
}
