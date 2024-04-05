package ch.cern.todo.service;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.request.TaskCategoryRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;


@Service
public class TaskCategoryService {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    public TaskCategory createTaskCategory(TaskCategoryRequest taskCategoryRequest) {
        final TaskCategory newTaskCategory = new TaskCategory(taskCategoryRequest.categoryName(), taskCategoryRequest.categoryDescription());
        return taskCategoryRepository.save(newTaskCategory);
    }

    public List<TaskCategory> getAllCategories() {
        return taskCategoryRepository.findAll();
    }

    public Optional<TaskCategory> getTaskCategoryById(Long categoryId) {
        return taskCategoryRepository.findById(categoryId);
    }

    public TaskCategory updateTaskCategory(TaskCategoryRequest taskCategoryRequest, Long categoryId) {
        // TODO -> Refactor this
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(categoryId);

        // Check if categoryName is already taken -> refactor this
        if (taskCategory.isEmpty()) {
            throw new IllegalArgumentException("Task category with ID " + categoryId + " not found");
        }

        TaskCategory categoryToUpdate = taskCategory.get();

        categoryToUpdate.setCategoryName(taskCategoryRequest.categoryName());
        categoryToUpdate.setCategoryDescription(taskCategoryRequest.categoryDescription());

        return taskCategoryRepository.save(categoryToUpdate);
    }

    public void deleteTaskCategory(Long categoryId) {
        // TODO -> Add exceptions
        taskCategoryRepository.deleteById(categoryId);
    }
}
