package ch.cern.todo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_name", length = 100)
    private String taskName;

    @Column(name = "task_description", length = 200)
    private String taskDescription;

    @Column(name = "deadline")
    // TODO -> check if it shouldn't be a timestamp
    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private TaskCategory category;
}
