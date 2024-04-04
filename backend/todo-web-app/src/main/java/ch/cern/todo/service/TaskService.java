package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Task task) {
        // Ensure the task exists in the database before updating
        Task existingTask = taskRepository.findById(task.getTaskId())
                .orElseThrow(() -> new NotFoundException("Task not found"));
        existingTask.setTaskName(task.getTaskName());
        existingTask.setTaskDescription(task.getTaskDescription());
        existingTask.setDeadline(task.getDeadline());
        existingTask.setCategory(task.getCategory());
        return taskRepository.save(existingTask);
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
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }
}
