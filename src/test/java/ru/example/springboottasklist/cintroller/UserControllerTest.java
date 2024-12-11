package ru.example.springboottasklist.cintroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.example.springboottasklist.TestContainerInitializer;
import ru.example.springboottasklist.dto.UpdateUserDto;
import ru.example.springboottasklist.dto.UserDto;
import ru.example.springboottasklist.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends TestContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final JSONObject jsonObject = new JSONObject();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final String phone = "+7 (000) 000-00-00";

    private static Stream<Arguments> updateUserTestNotValidParams() throws Exception {
        String json1 = objectMapper.writeValueAsString(new UpdateUserDto("a", lastName, phone));
        String json2 = objectMapper.writeValueAsString(new UpdateUserDto("aoqwertyuiasdfghjkl;", firstName, phone));
        String json3 = objectMapper.writeValueAsString(new UpdateUserDto(firstName, "f", phone));
        String json4 = objectMapper.writeValueAsString(new UpdateUserDto(firstName, "qwertyuiop[asdfghjk", phone));
        String json5 = objectMapper.writeValueAsString(new UpdateUserDto(firstName, lastName, "1234567890"));

        return Stream.of(
                Arguments.of(json1, 400),
                Arguments.of(json2, 400),
                Arguments.of(json3, 400),
                Arguments.of(json4, 400),
                Arguments.of(json5, 400)

        );
    }

    @ParameterizedTest
    @MethodSource("updateUserTestNotValidParams")
    @DisplayName("Проверка обновления пользователя, когда поле не валидно")
    public void givenUpdateUserDtoAndNotValidParams_whenUpdate_thenReturnBadRequest(String JsonString, int expectedResponse) throws Exception {
        MockHttpServletResponse responsePost = mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(expectedResponse);

    }


    @Test
    @DisplayName("Обновление информации об авторизованном пользователе. Код ответа 200")
    void givenJSONAndRoleUser_whenPatchUser_thenReturnIsOk() throws Exception {

        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("phone", phone);

        mockMvc.perform(
                        patch("/users/me")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.phone").value(phone));


    }

    @Test
    @DisplayName("Обновление информации пользователя не авторизованным пользователем. Код ответа 200")
    void givenJSONAndUnauthorizedUser_whenPatchUser_thenReturnIsUnauthorized() throws Exception {

        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("phone", phone);

        mockMvc.perform(
                        patch("/users/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("Получение информации об авторизованном пользователе. Код ответа 200")
    void givenAuthorizedUser_whenGetUser_thenReturnOk() throws Exception {
        var user = userRepository.findByLogin("user1@gmail.com");
        UserDto userDto = new UserDto(user.get().getId(), user.get().getLogin(), user.get().getFirstName(), user.get().getLastName(), user.get().getPhone(), user.get().getRole());

        mockMvc.perform(get("/users/me")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth(
                                "user1@gmail.com",
                                "password", StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.username").value(userDto.username()))
                .andExpect(jsonPath("$.firstName").value(userDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.lastName()))
                .andExpect(jsonPath("$.phone").value(userDto.phone()))
                .andExpect(jsonPath("$.role").value(String.valueOf(userDto.role())));

    }

    @Test
    @DisplayName("Получение информации об авторизованном пользователе не авторизованным пользователем. Код ответа 401")
    void givenUnauthorizedUser_whenGetUser_thenReturnIsUnauthorized() throws Exception {

        mockMvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("Обновление пароля авторизованного пользователя. Код ответа 204")
    void givenJSONAndRoleUser_whenPatchPassword_thenReturnIsNoContent() throws Exception {

        jsonObject.put("currentPassword", "password");
        jsonObject.put("newPassword", "password1");

        mockMvc.perform(
                        post("/users/set_password")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isNoContent());
        assertThat(userRepository.findByLogin("user1@gmail.com")).isPresent().get().satisfies(currentUser -> {
            assertTrue(passwordEncoder.matches("password1", currentUser.getPassword()));
        });

    }

    @Test
    @DisplayName("Обновление пароля не авторизованным пользователем. Код ответа 401")
    void givenJSONAndUnauthorizedUser_whenPatchPassword_thenReturnIsUnauthorized() throws Exception {

        jsonObject.put("currentPassword", "password");
        jsonObject.put("newPassword", "password1");

        mockMvc.perform(
                        post("/users/set_password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("Обновление пароля авторизованным пользователем с неверным текущим паролем. Код ответа 400")
    void givenWrongJSONAndRoleUser_whenPatchPassword_thenReturnIsUnauthorized() throws Exception {

        jsonObject.put("currentPassword", "passwwwword");
        jsonObject.put("newPassword", "password1");

        mockMvc.perform(
                        post("/users/set_password")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isBadRequest());
        assertThat(userRepository.findByLogin("user1@gmail.com")).isPresent().get().satisfies(currentUser -> {
            assertFalse(passwordEncoder.matches("passwwwword", currentUser.getPassword()));
        });

    }

}
