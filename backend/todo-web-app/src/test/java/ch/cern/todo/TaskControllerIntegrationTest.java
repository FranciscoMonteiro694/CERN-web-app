package ch.cern.todo;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.request.TaskRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskCategoryRepository.deleteAll();
    }

    @Test
    public void givenTasks_whenGetAllTasks_thenListAllTasks() throws Exception {
        final TaskCategory category = new TaskCategory("Category 1", "Description 1");

        taskCategoryRepository.save(category);

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        List<Task> tasks = List.of(
                new Task("Task 1", "Description 1", tomorrow, category),
                new Task("Task 2", "Description 2", tomorrow, category));

        taskRepository.saveAll(tasks);

        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].taskName").value("Task 1"))
                .andExpect(jsonPath("$[0].taskDescription").value("Description 1"))
                .andExpect(jsonPath("$[1].taskName").value("Task 2"))
                .andExpect(jsonPath("$[1].taskDescription").value("Description 2"))
                .andExpect(jsonPath("$", hasSize(2)));

    }
    @Test
    public void givenTask_whenCreatingNewTask_thenNewTaskIsCreated() throws Exception {
        final TaskCategory category = new TaskCategory("Category 1", "Description 1");

        taskCategoryRepository.save(category);

        final TaskRequest taskRequest = new TaskRequest("Task 1", "Description 1", "2025-04-07T10:15:30", category.getCategoryId());
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    public void givenTask_whenUpdatingTask_thenTaskIsUpdated() throws Exception {
        final TaskCategory category = new TaskCategory("Category 1", "Description 1");

        taskCategoryRepository.save(category);

        final Task task = new Task("Task 1", "Description 1", LocalDateTime.now().plusDays(1), category);
        taskRepository.save(task);

        final TaskRequest taskRequest = new TaskRequest("Task 2", "Description 2", "2025-04-07T10:15:30", category.getCategoryId());

        mockMvc.perform(put("/tasks/{taskId}", task.getTaskId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenTaskId_whenDeletingTask_thenTaskIsDeleted() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 1", "Description 1");
        taskCategoryRepository.save(taskCategory);

        final Task task = new Task("Task 1", "Description 1", LocalDateTime.now().plusDays(1), taskCategory);
        taskRepository.save(task);

        mockMvc.perform(delete("/tasks/{taskId}", task.getTaskId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenTaskId_whenRetrievingTask_thenTaskIsListed() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 6", "Description 6");
        taskCategoryRepository.save(taskCategory);

        final Task task = new Task("Task 1", "Description 1", LocalDateTime.now().plusDays(1), taskCategory);
        taskRepository.save(task);

        mockMvc.perform(get("/tasks/{taskId}", task.getTaskId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenTaskNameExceeding100Chars_whenCreatingNewTask_thenTaskIsNotCreated() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 6", "Description 6");
        taskCategoryRepository.save(taskCategory);

        final TaskRequest taskRequest = new TaskRequest("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean ma", "Description 2", "2025-04-07T10:15:30", taskCategory.getCategoryId());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTaskDescriptionExceeding500Chars_whenCreatingNewTask_thenTaskIsNotCreated() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 6", "Description 6");
        taskCategoryRepository.save(taskCategory);

        final TaskRequest taskRequest = new TaskRequest("Task 1", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus.", "2025-04-07T10:15:30", taskCategory.getCategoryId());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTaskDeadlineInWrongFormat_whenCreatingNewTask_thenTaskIsNotCreated() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 6", "Description 6");
        taskCategoryRepository.save(taskCategory);

        final TaskRequest taskRequest = new TaskRequest("Task 1", "Description 2", "2025-04-00    10:15:30", taskCategory.getCategoryId());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void givenTaskCategoryDoesNotExist_whenCreatingNewTask_thenTaskIsNotCreated() throws Exception {
        final TaskRequest taskRequest = new TaskRequest("Task 1", "Description 2", "2025-04-00T10:15:30", 20L);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenTaskIdThatDoesNotExist_whenDeletingTask_thenTaskIsNotDeleted() throws Exception {

        mockMvc.perform(get("/tasks/125")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}
