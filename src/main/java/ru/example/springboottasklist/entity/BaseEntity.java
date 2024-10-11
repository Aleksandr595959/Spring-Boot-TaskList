package ru.example.springboottasklist.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
}
