package ch.cern.todo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "task_categories")
public class TaskCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 100, unique = true)
    private String categoryName;

    @Column(name = "category_description", length = 500)
    private String categoryDescription;
}
