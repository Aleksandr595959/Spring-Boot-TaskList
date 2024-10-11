package ru.example.springboottasklist.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import ru.example.springboottasklist.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {


    @Column(name = "login", nullable = false, unique = true)
    private String login;


    @Column(name = "password", nullable = false)
    private String password;


    @Column
    private String lastName;


    @Column
    private String firstName;


    @Column
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


    @CreatedDate
    @Column
    private LocalDateTime dateOfCreated = LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;
}
