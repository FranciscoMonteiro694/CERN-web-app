package ch.cern.todo.controller;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.request.TaskCategoryRequest;
import ch.cern.todo.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class TaskCategoryController {

    @Autowired
    private TaskCategoryService taskCategoryService;

    @PostMapping
    public ResponseEntity<TaskCategory> createTaskCategory(@RequestBody TaskCategoryRequest taskCategoryRequest) {
        final TaskCategory taskCategory = taskCategoryService.createTaskCategory(taskCategoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskCategory);
    }

    @GetMapping
    public ResponseEntity<List<TaskCategory>> retrieveAllCategories() {
        final List<TaskCategory> taskCategories = taskCategoryService.getAllCategories();
        return ResponseEntity.ok(taskCategories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<TaskCategory> getTaskCategoryById(@PathVariable Long categoryId) {
        // TODO -> Refactor this
        final Optional<TaskCategory> taskCategory = taskCategoryService.getTaskCategoryById(categoryId);
        return taskCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<TaskCategory> updateTaskCategory(@PathVariable Long categoryId, @RequestBody TaskCategoryRequest taskCategoryRequest) {
        TaskCategory updatedTaskCategory = taskCategoryService.updateTaskCategory(taskCategoryRequest, categoryId);
        return ResponseEntity.ok(updatedTaskCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteTaskCategory(@PathVariable Long categoryId) {
        taskCategoryService.deleteTaskCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
