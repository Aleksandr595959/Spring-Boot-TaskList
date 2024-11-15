package ru.example.springboottasklist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.example.springboottasklist.dto.NewPasswordDto;
import ru.example.springboottasklist.dto.UpdateUserDto;
import ru.example.springboottasklist.dto.UserDto;
import ru.example.springboottasklist.service.UserService;

import javax.validation.Valid;

/**
 * Контроллер отвечающий за обработку HTTP-запросов, связанных с пользователями.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Пользователи")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * Обновляет пароль авторизованного пользователя.
     *
     * @param newPasswordDto DTO-объект с текущим и новым паролем.
     * @return Ответ со статусом HTTP 204 (No Content).
     */
    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<Void> setPassword(@RequestBody @Valid NewPasswordDto newPasswordDto) {
        userService.setPassword(newPasswordDto.currentPassword(), newPasswordDto.newPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает информацию об авторизованном пользователе.
     *
     * @return Ответ со статусом HTTP 200 и DTO-объектом пользователя в теле ответа.
     */
    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UserDto> getUser() {
        return ResponseEntity.ok(userService.getAuthorizedUser());
    }

    /**
     * Обновляет информацию об авторизованном пользователе.
     *
     * @param updateUser DTO с обновленными данными пользователя.
     * @return Ответ со статусом HTTP 200 OK и обновленной информацией об авторизованном пользователе в теле ответа.
     */
    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody @Valid UpdateUserDto updateUser) {
        return ResponseEntity.ok(userService.updateUser(updateUser));
    }


}
