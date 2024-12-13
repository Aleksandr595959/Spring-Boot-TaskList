package ru.example.springboottasklist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.example.springboottasklist.TestContainerInitializer;
import ru.example.springboottasklist.dto.CategoriesDto;
import ru.example.springboottasklist.dto.CategoryDto;
import ru.example.springboottasklist.dto.CreateOrUpdateCategoryDto;
import ru.example.springboottasklist.exeption.CategoryNotFoundException;
import ru.example.springboottasklist.mapper.CategoryMapper;
import ru.example.springboottasklist.repository.CategoryRepository;
import ru.example.springboottasklist.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryControllerTest extends TestContainerInitializer {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserRepository userRepository;

    private static final String categoryTitle = "CategoryTest1";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final JSONObject jsonObject = new JSONObject();

    private static Stream<Arguments> createCategoryTestNotValidParams() throws Exception {
        String categoryJson1 = objectMapper.writeValueAsString(new CreateOrUpdateCategoryDto("s"));
        String categoryJson2 = objectMapper.writeValueAsString(new CreateOrUpdateCategoryDto("fasjhfjsadhfjassfdgsdgsdgsdgsdgsdgsdgsdgsdgsdgsdgsdgsdgsdgdkfaskfsajfksajfjlksjkfjlskajksfdjkjsjdjksjdfkjskjkjfdskjfkjkjdskjfkjdskj"));


        return Stream.of(
                Arguments.of(categoryJson1, 400),
                Arguments.of(categoryJson2,400)

        );
    }

    @ParameterizedTest
    @MethodSource("createCategoryTestNotValidParams")
    @DisplayName("Проверка создания категории, когда поле не валидно")
    public void givenRegisterDtoAndNotValidParams_whenRegister_thenReturnBadRequest(String createJsonString, int expectedResponse) throws Exception {
         Long categoryCount = categoryRepository.count();
        MockHttpServletResponse responsePost = mockMvc.perform(MockMvcRequestBuilders
                        .post("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonString)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(expectedResponse);
       assertThat(categoryRepository.count()).isEqualTo(categoryCount);
    }

    @Test
    @DisplayName("Изменение несуществующей категории админом. Код ответа 404")
    void givenJSONAndWrongCategoryIdAndRoleAdmin_whenPatchCategory_thenReturnIsNotFound() throws Exception {

        jsonObject.put("categoryTitle", "Category Up");

        mockMvc.perform(
                        patch("/category/999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Изменение категории админом. Код ответа 200")
    void givenJSONAndRoleAdmin_whenPatchCategory_thenReturnIsOk() throws Exception {

        jsonObject.put("categoryTitle", "Category Up");

        mockMvc.perform(
                        patch("/category/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isOk());

        assertThat(categoryRepository.findById(1L).orElseThrow().getCategoryTitle()).isEqualTo("Category Up");

    }

    @Test
    @DisplayName("Изменение категории не создателем категории. Код ответа 403")
    void givenJSONAndCategoryCreator_whenPatchCategory_thenReturnIsForbidden() throws Exception {

        jsonObject.put("categoryTitle", "Category Up");

        mockMvc.perform(
                        patch("/category/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user2@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Изменение категории создателем категории. Код ответа 200")
    void givenJSONAndCategoryCreator_whenPatchCategory_thenReturnIsOk() throws Exception {

        jsonObject.put("categoryTitle", "Category Up");

        mockMvc.perform(
                        patch("/category/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isOk());

        assertThat(categoryRepository.findById(1L).orElseThrow().getCategoryTitle()).isEqualTo("Category Up");

    }

    @Test
    @DisplayName("Изменение категории не авторизованным пользователем. Код ответа 401")
    void givenJSONAndUnauthorizedUser_whenPatchCategory_thenReturnUnauthorized() throws Exception {

        jsonObject.put("categoryTitle", "Category Up");

        mockMvc.perform(
                        patch("/category/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("Получение категорий неавторизованного пользователя. Код ответа 401")
    void givenUnauthorizedUser_whenGetCategories_thenReturnUnauthorized() throws Exception {

        mockMvc.perform(get("/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


    }

    @Test
    @DisplayName("Получение категорий авторизованного пользователя. Код ответа 200")
    void givenAuthorizedUser_whenGetCategories_thenReturnOk() throws Exception {

        CategoriesDto categoriesDto = userRepository.findById(1L)
                .map(user -> {
                    List<CategoryDto> categoryDto = user.getCategories().stream()
                            .map(categoryMapper::toCategoryDto)
                            .toList();
                    return new CategoriesDto(categoryDto);
                })
                .orElseThrow(() -> new CategoryNotFoundException("dd"));


        mockMvc.perform(get("/category")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user1@gmail.com", "password", StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].categoryTitle").value(categoriesDto.categories().get(0).categoryTitle()))
                .andExpect(jsonPath("$.categories[1].categoryTitle").value(categoriesDto.categories().get(1).categoryTitle()));

    }


    @Test
    @DisplayName("Добавление категории админом. Код ответа 200")
    void whenAdminAddsCategory_withValidJson_thenReturnsOk() throws Exception {

        jsonObject.put("categoryTitle", "categoryTitleAdmin");

        int categoryCount = categoryRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString())
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "admin@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(categoryRepository.findAll().size()).isEqualTo(categoryCount + 1);
        assertThat(categoryRepository.findAll().stream()
                .filter(category -> category.getCategoryTitle().equals("categoryTitleAdmin"))
                .findFirst()
                .orElseThrow()
                .getUser()
                .getFirstName())
                .isEqualTo("AdminFirstName");
    }

    @Test
    @DisplayName("Добавление категории юзером. Код ответа 200")
    @WithMockUser()
    void whenUserAddsCategory_withValidJson_thenReturnsOk() throws Exception {

        jsonObject.put("categoryTitle", "categoryTitle200");

        int categoryCount = categoryRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString())
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(categoryRepository.findAll().size()).isEqualTo(categoryCount + 1);
        assertThat(categoryRepository.findAll().stream()
                .filter(category -> category.getCategoryTitle().equals("categoryTitle200"))
                .findFirst()
                .orElseThrow()
                .getUser()
                .getFirstName())
                .isEqualTo("User1FirstName");
    }

    @Test
    @DisplayName("Добавление категории неавторизованным пользователем. Код ответа 401")
    void whenUnauthenticatedUserAttemptsToAddCategory_withValidJson_thenReturnsUnauthorized() throws Exception {

        jsonObject.put("categoryTitle", "categoryTitle200");

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @DisplayName("Удаление категории админом по идентификатору категории. Код ответа 204")
    void givenCategoryIdAndRoleAdmin_whenDeleteCategory_thenReturnIsNoContent() throws Exception {
        int categorySize = categoryRepository.findAll().size();
        mockMvc.perform(
                        delete("/category/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isNoContent());

        assertThat(categoryRepository.findAll().size()).isEqualTo(categorySize - 1);

    }

    @Test
    @DisplayName("Удаление категории пользователем по идентификатору категории. Код ответа 204")
    void givenCategoryIdAndRoleUser_whenDeleteCategory_thenReturnIsNoContent() throws Exception {
        int categorySize = categoryRepository.findAll().size();
        mockMvc.perform(
                        delete("/category/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isNoContent());

        assertThat(categoryRepository.findAll().size()).isEqualTo(categorySize - 1);

    }

    @Test
    @DisplayName("Удаление категории пользователем по не правильному идентификатору категории. Код ответа 404")
    void givenWrongCategoryIdAndRoleUser_whenDeleteCategory_thenReturnIsNotFound() throws Exception {
        int categorySize = categoryRepository.findAll().size();
        mockMvc.perform(
                        delete("/category/99999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isNotFound());
        assertThat(categoryRepository.findAll().size()).isEqualTo(categorySize);

    }

    @Test
    @DisplayName("Удаление категории по идентификатору категории не создателем категории. Код ответа 403")
    void givenNonCreator_whenDeleteCategory_thenReturnIsForbidden() throws Exception {
        int categorySize = categoryRepository.findAll().size();
        mockMvc.perform(
                        delete("/category/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user2@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isForbidden());
        assertThat(categoryRepository.findAll().size()).isEqualTo(categorySize);

    }

    @Test
    @DisplayName("Удаление категории по идентификатору категории неавторизованным пользователем. Код ответа 401")
    void givenUnauthorizedUser_whenDeleteCategory_thenReturnIsUnauthorized() throws Exception {
        int categorySize = categoryRepository.findAll().size();
        mockMvc.perform(
                        delete("/category/1"))

                .andExpect(status().isUnauthorized());
        assertThat(categoryRepository.findAll().size()).isEqualTo(categorySize);

    }

}