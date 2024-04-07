package ch.cern.todo.service;

import ch.cern.todo.exceptions.TaskCategoryDoesNotExistException;
import ch.cern.todo.exceptions.TaskDoesNotExistException;
import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.request.TaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @InjectMocks
    private TaskService victim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateNewTask_whenNewTaskRequestIsMade() {
        TaskRequest taskRequest = new TaskRequest("Task 1", "Description 1", "2025-05-01T10:10:10", 1L);
        TaskCategory taskCategory = new TaskCategory("Category 1", "Description 1");
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), taskCategory);
        when(taskCategoryRepository.findById(1L)).thenReturn(Optional.of(taskCategory));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = victim.createTask(taskRequest);

        assertEquals("Task 1", createdTask.getTaskName());
        assertEquals("Description 1", createdTask.getTaskDescription());
        assertEquals(taskCategory, createdTask.getCategory());
    }

    @Test
    void shouldListTasks_whenListTaskRequestIsMade() {
        List<Task> tasks = List.of(new Task("Task 1", "Description 1", LocalDateTime.now(), new TaskCategory("Category 1", "Description 1")),
                new Task("Task 2", "Description 2", LocalDateTime.now(), new TaskCategory("Category 2", "Description 2")));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> retrievedTasks = victim.getAllTasks();

        assertEquals(2, retrievedTasks.size());
    }

    @Test
    void shouldListTask_whenGetTaskByIdRequestIsMade() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), new TaskCategory("Category 1", "Description 1"));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task retrievedTask = victim.getTaskById(1L);

        assertEquals("Task 1", retrievedTask.getTaskName());
        assertEquals("Description 1", retrievedTask.getTaskDescription());
    }

    @Test
    void shouldUpdateTask_whenUpdateTaskRequestIsMade() {
        TaskCategory taskCategory = new TaskCategory("Category 1", "Description 1");
        Task taskToUpdate = new Task("Task 1", "Description 1", LocalDateTime.now(), taskCategory);
        TaskRequest updatedTaskRequest = new TaskRequest("Updated Task", "Updated Description", "2025-05-01T10:10:10", 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskToUpdate));
        when(taskCategoryRepository.findById(1L)).thenReturn(Optional.of(taskCategory));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = victim.updateTask(updatedTaskRequest, 1L);

        assertEquals("Updated Task", updatedTask.getTaskName());
        assertEquals("Updated Description", updatedTask.getTaskDescription());
    }

    @Test
    void shouldDeleteTask_whenDeleteTaskRequestIsMade() {
        Task taskToDelete = new Task("Task 1", "Description 1", LocalDateTime.now(), new TaskCategory("Category 1", "Description 1"));
        taskRepository.save(taskToDelete);
        when(taskRepository.existsById(1L)).thenReturn(true);

        victim.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldThrowTaskCategoryDoesNotExistException_whenCreatingTaskWithCategoryThatDoesNotExist() {
        TaskRequest taskRequest = new TaskRequest("Task 1", "Description 1", "2025-05-01T10:10:10", 1L);
        when(taskCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskCategoryDoesNotExistException.class, () -> victim.createTask(taskRequest));
    }

    @Test
    void shouldThrowTaskDoesNotExistException_whenRetrievingTaskWithIdThatDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskDoesNotExistException.class, () -> victim.getTaskById(1L));
    }

    @Test
    void shouldThrowTaskDoesNotExistException_whenUpdatingTaskWithTaskIdThatDoesNotExist() {
        TaskRequest taskRequest = new TaskRequest("Task 1", "Description 1", "2025-05-01T10:10:10", 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskDoesNotExistException.class, () -> victim.updateTask(taskRequest, 1L));
    }

    @Test
    void shouldThrowTaskCategoryDoesNotExistException_whenUpdatingTaskWithCategoryIdThatDoesNotExist() {
        TaskCategory taskCategory = new TaskCategory("Category 1", "Description 1");
        TaskRequest taskRequest = new TaskRequest("Task 1", "Description 1", "2025-05-01T10:10:10", 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));
        when(taskCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskCategoryDoesNotExistException.class, () -> victim.updateTask(taskRequest, 1L));
    }

    @Test
    void shouldThrowTaskDoesNotExistException_whenDeletingTaskWithIdThatDoesNotExist() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskDoesNotExistException.class, () -> victim.deleteTask(1L));
    }
}