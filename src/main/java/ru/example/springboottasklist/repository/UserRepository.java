package ru.example.springboottasklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.springboottasklist.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по логину.
     *
     * @param login логин пользователя
     * @return Optional с найденным пользователем или пустым значением, если пользователь не найден
     */
    Optional<User> findByLogin(String login);

    /**
     * Проверяет существование пользователя по логину.
     *
     * @param login логин пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByLogin(String login);
}
