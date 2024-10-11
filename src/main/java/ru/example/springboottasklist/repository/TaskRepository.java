package ru.example.springboottasklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.entity.Task;
import ru.example.springboottasklist.entity.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> , JpaSpecificationExecutor<Task> {
    boolean existsById(Long id);


}
