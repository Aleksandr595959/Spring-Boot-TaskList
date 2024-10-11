package ru.example.springboottasklist.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stat")
public class Stat extends BaseEntity {

    @Column
    private Long completedTaskCount;

    @Column
    private Long uncompletedTaskCount;
}
