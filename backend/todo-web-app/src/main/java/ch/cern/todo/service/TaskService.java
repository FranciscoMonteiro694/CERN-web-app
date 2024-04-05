package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.request.TaskRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Transactional
    public Task createTask(TaskRequest taskRequest) {
        // TODO - refactor this
        final TaskCategory existingCategory = taskCategoryRepository.findById(taskRequest.categoryId()).orElse(null);

        final Task newTask = new Task(taskRequest.taskName(), taskRequest.taskDescription(), taskRequest.deadline(), existingCategory);
        return taskRepository.save(newTask);
    }
/*

    @Transactional
    public Task updateTask(Long taskId, Task task) {
        // Ensure the task exists in the database before updating
        Task existingTask = taskRepository.findById(task.getTaskId())
                .orElseThrow(() -> new NotFoundException("Task not found"));
        existingTask.setTaskName(task.getTaskName());
        existingTask.setTaskDescription(task.getTaskDescription());
        existingTask.setDeadline(task.getDeadline());
        existingTask.setCategory(task.getCategory());
        return taskRepository.save(existingTask);
        return null;
    }

    @Transactional
    public void deleteTask(Long taskId) {
        // Ensure the task exists in the database before deleting
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        taskRepository.delete(existingTask);

    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();

        return null;
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        return null;
    }
    */
}
