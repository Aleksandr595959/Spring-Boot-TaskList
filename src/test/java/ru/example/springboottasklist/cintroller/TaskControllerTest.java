package ru.example.springboottasklist.cintroller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.example.springboottasklist.TestContainerInitializer;
import ru.example.springboottasklist.dto.CreateOrUpdateTaskDto;
import ru.example.springboottasklist.dto.FilterPageTaskResponseDto;
import ru.example.springboottasklist.dto.TaskDto;
import ru.example.springboottasklist.dto.TasksDto;
import ru.example.springboottasklist.enums.PriorityTitle;
import ru.example.springboottasklist.enums.Status;
import ru.example.springboottasklist.mapper.TaskMapper;
import ru.example.springboottasklist.repository.CategoryRepository;
import ru.example.springboottasklist.repository.TaskRepository;
import ru.example.springboottasklist.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.example.springboottasklist.enums.PriorityTitle.HIGH;
import static ru.example.springboottasklist.enums.Status.IN_PROGRESS;

public class TaskControllerTest extends TestContainerInitializer {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JSONObject jsonObject = new JSONObject();

    private static final String taskTitle = "TaskTest99";
    private static final PriorityTitle title = HIGH;
    private static final Status status = IN_PROGRESS;
    private static final Long categoryId = 2L;

    private static Stream<Arguments> createTaskTestNotValidParams() throws Exception {
        String taskJson1 = objectMapper.writeValueAsString(new CreateOrUpdateTaskDto("qwe", title, categoryId, status));
        String taskJson2 = objectMapper.writeValueAsString(new CreateOrUpdateTaskDto("qwertyuiop;lkjhgfdsazxcvbnm,./qwe", title, categoryId, status));

        return Stream.of(
                Arguments.of(taskJson1, 400),
                Arguments.of(taskJson2, 400)

        );
    }

    private static Stream<Arguments> filteredTasksTest() {
        return Stream.of(
                Arguments.of("Task1", null, null, null, 200, null, null),
                Arguments.of(null, "LOW", null, null, 200, null, null),
                Arguments.of(null, null, "DONE", null, 200, null, null),
                Arguments.of(null, null, null, "Category 2", 200, null, null),
                Arguments.of(null, null, null, null, 200, "2023-11-12T14:30:00.000000", null),
                Arguments.of(null, null, null, null, 200, null, "2024-10-12T14:30:00.000000"),
                Arguments.of(null, null, null, null, 200, "2021-11-12T14:30:00.000000", "2024-10-12T14:30:00.000000")


        );
    }

    @ParameterizedTest
    @MethodSource("filteredTasksTest")
    @DisplayName("Проверка фильтрации задач")
    public void givenFilterParams_whenGetTaskUser_thenReturnFilteredTasks(String taskTitleTest, String priorityTitle, String statusTest, String categoryTitleTest, int expectedResponse, String timeFrom, String timeTo) throws Exception {
        mockMvc.perform(get("/task/getTaskUser")
                        .param("page", "0")
                        .param("size", "10")
                        .param("taskTitle", taskTitleTest)
                        .param("title", priorityTitle)
                        .param("status", statusTest)
                        .param("categoryTitle", categoryTitleTest)
                        .param("timeFrom", timeFrom)
                        .param("timeTo", timeTo)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(expectedResponse);
                    var returnedTaskList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<List<FilterPageTaskResponseDto>>() {
                            });
                    for (FilterPageTaskResponseDto filterPageTaskResponseDto : returnedTaskList) {
                        if (taskTitleTest != null) {
                            assertThat(filterPageTaskResponseDto.taskTitle()).isEqualTo(taskTitleTest);
                        }
                        if (priorityTitle != null) {
                            assertThat(filterPageTaskResponseDto.title().toString()).isEqualTo(priorityTitle);
                        }
                        if (statusTest != null) {
                            assertThat(filterPageTaskResponseDto.status().toString()).isEqualTo(statusTest);
                        }
                        if (categoryTitleTest != null) {
                            assertThat(categoryRepository.findById(filterPageTaskResponseDto.categoryId()).get().getCategoryTitle()).isEqualTo(categoryTitleTest);
                        }
                        if (timeFrom != null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                            LocalDateTime dateTime = LocalDateTime.parse(filterPageTaskResponseDto.createdTask(), formatter);
                            assertThat(dateTime).isAfter(timeFrom);
                        }
                        if (timeTo != null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                            LocalDateTime dateTime1 = LocalDateTime.parse(filterPageTaskResponseDto.createdTask(), formatter);
                            assertThat(dateTime1).isBefore(timeTo);
                        }
                    }

                });

    }

    @ParameterizedTest
    @MethodSource("createTaskTestNotValidParams")
    @DisplayName("Проверка создания задачи, когда поле не валидно")
    public void givenRegisterDtoAndNotValidParams_whenRegister_thenReturnBadRequest(String createJsonString, int expectedResponse) throws Exception {
        Long taskCount = taskRepository.count();
        MockHttpServletResponse responsePost = mockMvc.perform(MockMvcRequestBuilders
                        .post("/task/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJsonString)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(expectedResponse);
        assertThat(taskRepository.count()).isEqualTo(taskCount);
    }

    @Test
    @DisplayName("Добавление задачи неавторизованным пользователем. Код ответа 401")
    void whenUnauthenticatedUserAttemptsToAddTask_withValidJson_thenReturnsUnauthorized() throws Exception {

        jsonObject.put("taskTitle", taskTitle);
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/task/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @DisplayName("Добавление задачи юзером. Код ответа 200")
    @WithMockUser()
    void whenUserAddsTask_withValidJson_thenReturnsOk() throws Exception {

        jsonObject.put("taskTitle", taskTitle);
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);


        int taskCount = taskRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/task/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString())
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(taskRepository.findAll().size()).isEqualTo(taskCount + 1);
        assertThat(taskRepository.findAll().stream()
                .filter(task -> task.getTaskTitle().equals(taskTitle))
                .findFirst()
                .orElseThrow()
                .getUser()
                .getFirstName())
                .isEqualTo("User1FirstName");
    }

    @Test
    @DisplayName("Добавление задачи админом. Код ответа 200")
    @WithMockUser()
    void whenAdminAddsTask_withValidJson_thenReturnsOk() throws Exception {

        jsonObject.put("taskTitle", taskTitle);
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);


        int taskCount = taskRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/task/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toJSONString())
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "admin@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(taskRepository.findAll().size()).isEqualTo(taskCount + 1);
        assertThat(taskRepository.findAll().stream()
                .filter(task -> task.getTaskTitle().equals(taskTitle))
                .findFirst()
                .orElseThrow()
                .getUser()
                .getFirstName())
                .isEqualTo("AdminFirstName");
    }

    @Test
    @DisplayName("Удаление задачи админом по идентификатору задачи. Код ответа 204")
    void givenTaskIdAndRoleAdmin_whenDeleteTask_thenReturnIsNoContent() throws Exception {
        int taskSize = taskRepository.findAll().size();
        mockMvc.perform(
                        delete("/task/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isNoContent());

        assertThat(taskRepository.findAll().size()).isEqualTo(taskSize - 1);

    }

    @Test
    @DisplayName("Удаление задачи пользователем по идентификатору задачи. Код ответа 204")
    void givenTaskIdAndRoleUser_whenDeleteTask_thenReturnIsNoContent() throws Exception {
        int taskSize = taskRepository.findAll().size();
        mockMvc.perform(
                        delete("/task/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isNoContent());

        assertThat(taskRepository.findAll().size()).isEqualTo(taskSize - 1);

    }

    @Test
    @DisplayName("Удаление задачи пользователем по не правильному идентификатору задачи. Код ответа 404")
    void givenWrongTaskIdAndRoleUser_whenDeleteTask_thenReturnIsNotFound() throws Exception {
        int taskSize = taskRepository.findAll().size();
        mockMvc.perform(
                        delete("/task/9999999999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isNotFound());

        assertThat(taskRepository.findAll().size()).isEqualTo(taskSize);

    }

    @Test
    @DisplayName("Удаление задачи пользователем по идентификатору задачи не создателем задачи. Код ответа 403")
    void givenTaskIdAndNotCreator_whenDeleteTask_thenReturnIsForbidden() throws Exception {
        int taskSize = taskRepository.findAll().size();
        mockMvc.perform(
                        delete("/task/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user2@gmail.com",
                                                "password", StandardCharsets.UTF_8)))

                .andExpect(status().isForbidden());

        assertThat(taskRepository.findAll().size()).isEqualTo(taskSize);

    }

    @Test
    @DisplayName("Удаление задачи не авторизованным пользователем по идентификатору задачи. Код ответа 401")
    void givenTaskIdAndUnauthorizedUser_whenDeleteTask_thenReturnIsUnauthorized() throws Exception {
        int taskSize = taskRepository.findAll().size();
        mockMvc.perform(
                        delete("/task/1"))

                .andExpect(status().isUnauthorized());

        assertThat(taskRepository.findAll().size()).isEqualTo(taskSize);

    }

    @Test
    @DisplayName("Получение задач неавторизованного пользователя. Код ответа 401")
    void givenUnauthorizedUser_whenGetTasks_thenReturnUnauthorized() throws Exception {

        mockMvc.perform(get("/task/getAllTaskUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


    }

    @Test
    @DisplayName("Получение задач авторизованного пользователя. Код ответа 200")
    void givenAuthorizedUser_whenGetTasks_thenReturnOk() throws Exception {
        List<TaskDto> ListTaskDto = taskMapper.toTaskDto(userRepository.findById(1L).get().getTasks());
        TasksDto tasksDto = new TasksDto(ListTaskDto.size(), ListTaskDto);

        mockMvc.perform(get("/task/getAllTaskUser")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user1@gmail.com", "password", StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(tasksDto.count()))
                .andExpect(jsonPath("$.tasks[0].taskTitle").value(tasksDto.tasks().get(0).taskTitle()))
                .andExpect(jsonPath("$.tasks[0].categoryId").value(tasksDto.tasks().get(0).categoryId()))
                .andExpect(jsonPath("$.tasks[0].title").value(tasksDto.tasks().get(0).title().toString()))
                .andExpect(jsonPath("$.tasks[0].status").value(tasksDto.tasks().get(0).status().toString()));


    }

    @Test
    @DisplayName("Изменение несуществующей задачи админом. Код ответа 404")
    void givenJSONAndWrongTaskIdAndRoleAdmin_whenPatchTask_thenReturnIsNotFound() throws Exception {

        jsonObject.put("taskTitle", "taskTest");
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);


        mockMvc.perform(
                        patch("/task/999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Изменение  задачи админом. Код ответа 200")
    void givenJSONAndTaskIdAndRoleAdmin_whenPatchTask_thenReturnIsOk() throws Exception {

        jsonObject.put("taskTitle", "taskTest");
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);


        mockMvc.perform(
                        patch("/task/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isOk());
        assertThat(taskRepository.findById(1L).orElseThrow().getTaskTitle()).isEqualTo("taskTest");

    }

    @Test
    @DisplayName("Изменение  задачи не создателем задачи. Код ответа 403")
    void givenJSONAndTaskIdAndNotCreator_whenPatchTask_thenReturnIsForbidden() throws Exception {

        jsonObject.put("taskTitle", "taskTest");
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);


        mockMvc.perform(
                        patch("/task/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user2@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Изменение  задачи пользователем. Код ответа 200")
    void givenJSONAndTaskIdAndRoleUser_whenPatchTask_thenReturnIsOk() throws Exception {

        jsonObject.put("taskTitle", "taskTest");
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);


        mockMvc.perform(
                        patch("/task/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isOk());
        assertThat(taskRepository.findById(1L).orElseThrow().getTaskTitle()).isEqualTo("taskTest");

    }

    @Test
    @DisplayName("Изменение  задачи не авторизованным пользователем. Код ответа 401")
    void givenJSONAndTaskIdAndUnauthorizedUser_whenPatchTask_thenReturnIsUnauthorized() throws Exception {

        jsonObject.put("taskTitle", "taskTest");
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("status", status);
        jsonObject.put("title", title);


        mockMvc.perform(
                        patch("/task/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isUnauthorized());

    }


}
