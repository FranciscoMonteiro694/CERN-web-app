package ch.cern.todo;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.request.TaskCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskCategoryControllerIntegrationTest {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        taskCategoryRepository.deleteAll();
    }
    @Test
    public void givenCategories_whenGetAllCategories_thenListAllCategories() throws Exception {
        List<TaskCategory> categories = Arrays.asList(
                new TaskCategory("Category 1", "Description 1"),
                new TaskCategory("Category 2", "Description 2"));

        taskCategoryRepository.saveAll(categories);

        mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].categoryName").value("Category 1"))
                .andExpect(jsonPath("$[0].categoryDescription").value("Description 1"))
                .andExpect(jsonPath("$[1].categoryName").value("Category 2"))
                .andExpect(jsonPath("$[1].categoryDescription").value("Description 2"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void givenCategory_whenCreatingNewCategory_thenNewCategoryIsCreated() throws Exception {

        final TaskCategoryRequest taskCategoryRequest = new TaskCategoryRequest("Category 3", "Description 3");
        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskCategoryRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    public void givenCategory_whenUpdatingCategory_thenCategoryIsUpdated() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 4", "Description 4");
        taskCategoryRepository.save(taskCategory);

        final TaskCategoryRequest taskCategoryRequest = new TaskCategoryRequest("Category 5", "Description 5");

        mockMvc.perform(put("/categories/{categoryId}", taskCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCategoryRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenCategoryId_whenDeletingCategory_thenCategoryIsDeleted() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 12", "Description 12");
        taskCategoryRepository.save(taskCategory);

        mockMvc.perform(delete("/categories/{categoryId}", taskCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenCategoryId_whenRetrievingCategory_thenCategoryIsListed() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 6", "Description 6");
        taskCategoryRepository.save(taskCategory);

        System.out.println("here" + taskCategory.getCategoryId());
        mockMvc.perform(get("/categories/{categoryId}", taskCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenCategoryWithAlreadyExistingName_whenCreatingNewCategory_thenCategoryIsNotCreated() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 7", "Description 7");
        taskCategory.setCategoryId(7L);
        taskCategoryRepository.save(taskCategory);

        final TaskCategoryRequest taskCategoryRequest = new TaskCategoryRequest("Category 7", "Description 7");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCategoryRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void givenCategoryIdThatDoesNotExist_whenDeletingCategory_thenCategoryIsNotDeleted() throws Exception {
        final TaskCategory taskCategory = new TaskCategory("Category 8", "Description 8");
        taskCategory.setCategoryId(8L);
        taskCategoryRepository.save(taskCategory);

        mockMvc.perform(get("/categories/9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCategoryNameExceeding100Chars_whenCreatingNewCategory_thenCategoryIsNotCreated() throws Exception {
        final TaskCategoryRequest taskCategoryRequest = new TaskCategoryRequest("Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1Category 1", "Description 1");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCategoryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCategoryDescriptionExceeding500Chars_whenCreatingNewCategory_thenCategoryIsNotCreated() throws Exception {
        final TaskCategoryRequest taskCategoryRequest = new TaskCategoryRequest("Category 1", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCategoryRequest)))
                .andExpect(status().isBadRequest());
    }

}
