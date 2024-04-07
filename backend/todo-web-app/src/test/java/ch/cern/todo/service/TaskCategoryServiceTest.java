package ch.cern.todo.service;

import ch.cern.todo.exceptions.TaskCategoryAlreadyExistsException;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.request.TaskCategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskCategoryServiceTest {

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @InjectMocks
    private TaskCategoryService victim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateNewTaskCategory_whenNewTaskCategoryRequestIsMade() {
        TaskCategoryRequest taskCategoryRequest = new TaskCategoryRequest("Category 1", "Description 1");
        when(taskCategoryRepository.existsByCategoryName("Category 1")).thenReturn(false);
        when(taskCategoryRepository.save(any())).thenReturn(new TaskCategory());

        TaskCategory result = victim.createTaskCategory(taskCategoryRequest);

        assertNotNull(result);
    }

    @Test
    void shouldThrowTaskCategoryAlreadyExistsException_whenTaskCategorAlreadyExists() {
        TaskCategoryRequest taskCategoryRequest = new TaskCategoryRequest("Category 1", "Description 1");
        when(taskCategoryRepository.existsByCategoryName("Category 1")).thenReturn(true);

        assertThrows(TaskCategoryAlreadyExistsException.class, () -> victim.createTaskCategory(taskCategoryRequest));
    }

    @Test
    void shouldListTaskCategories_whenListTaskCategoriesRequestIsMade() {
        List<TaskCategory> categories = List.of(new TaskCategory(), new TaskCategory());
        when(taskCategoryRepository.findAll()).thenReturn(categories);

        List<TaskCategory> result = victim.getAllCategories();

        assertEquals(categories.size(), result.size());
    }

    @Test
    void shouldListTaskCategory_whenGetTaskCategoryByIdRequestIsMade() {
        TaskCategory category = new TaskCategory();
        when(taskCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

        TaskCategory result = victim.getTaskCategoryById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldDeleteTaskCategory_whenDeleteTaskCategoryRequestIsMade() {
        long categoryId = 1L;
        TaskCategory existingCategory = new TaskCategory("Existing Category", "Existing Description");
        existingCategory.setCategoryId(categoryId);

        when(taskCategoryRepository.existsById(categoryId)).thenReturn(true);

        victim.deleteTaskCategory(categoryId);

        verify(taskCategoryRepository, times(1)).deleteById(categoryId);
    }


}