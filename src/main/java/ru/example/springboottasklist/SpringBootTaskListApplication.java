package ru.example.springboottasklist;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "SpringBootTaskList API", version = "v1"))
public class SpringBootTaskListApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTaskListApplication.class, args);
    }

}
