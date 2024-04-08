import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { Task } from './task.model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TasksApiService } from './tasks-api.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TaskCategory } from './task-category.model';
import { CategoriesApiService } from './categories-api.service';
import { CreateCategoryDialogComponent } from './components/create-category-dialog/create-category-dialog.component';
import { CreateTaskDialogComponent } from './components/create-task-dialog/create-task-dialog.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatCardModule, MatButtonModule, MatIconModule, CommonModule, HttpClientModule, MatDialogModule, MatSnackBarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'webapp';

  tasks: Task[] = [];
  taskCategories: TaskCategory[] = [];

  constructor(private tasksApiService: TasksApiService, private categoriesApiService: CategoriesApiService, public dialog: MatDialog, private router: Router, private cdr: ChangeDetectorRef, private snackBar: MatSnackBar){ }

  ngOnInit(): void {
    this.loadTasks();
    this.loadTaskCategories();
  }

  openCreateTaskDialog(): void {
    const dialogRef = this.dialog.open(CreateTaskDialogComponent, {
      width: '500px',
      data: { categories: this.taskCategories }
    });

    dialogRef.afterClosed().subscribe((newTask: Task) => {
      if (newTask) {
        this.createTask(newTask);
      }
    });
  }

  openCreateCategoryDialog(): void {
    const dialogRef = this.dialog.open(CreateCategoryDialogComponent, {
      width: '500px',
    });

    dialogRef.afterClosed().subscribe((newCategory: TaskCategory) => {
      if (newCategory) {
        this.createCategory(newCategory);
      }
    });
  }

  createTask(task: Task): void {
    this.tasksApiService.createTask(task).subscribe(
      createdTask => {
        console.log('New task created:', createdTask);
        this.tasks.push(createdTask);
      },
      error => {
        console.error('Error creating task:', error);
        this.openSnackBar(error.error, 'Close');
      }
    )
  }

  createCategory(category: TaskCategory): void {
    this.categoriesApiService.createCategory(category).subscribe(
      createdCategory => {
        console.log('New category created:', createdCategory);
        this.taskCategories.push(createdCategory)
      },
      error => {
        console.error('Error creating category:', error);
        this.openSnackBar(error.error, 'Close');
      }
    );
  }

  loadTasks(): void {
    this.tasksApiService.getAllTasks().subscribe(tasks => {
      this.tasks = tasks;

      this.cdr.detectChanges();
    });
  }

  loadTaskCategories(): void {
    this.categoriesApiService.getAllCategories().subscribe(taskCategories => {
      this.taskCategories = taskCategories;

      this.cdr.detectChanges();
    });
  }

  deleteTask(taskId: number) {
    this.tasksApiService.deleteTask(taskId).subscribe(
      () => {
        console.log('Task deleted successfully');
        this.tasks = this.tasks.filter(task => task.taskId !== taskId);
      },
      (error) => {
        console.error('Error deleting task:', error);
        this.openSnackBar(error.error, 'Close');
      }
    );
  }

  deleteCategory(categroyId: number) {
    this.categoriesApiService.deleteCategory(categroyId).subscribe(
      () => {
        console.log('Category deleted successfully');
        this.taskCategories = this.taskCategories.filter(category => category.categoryId !== categroyId);

      },
      (error) => {
        console.error('Error deleting category:', error.error);
        this.openSnackBar(error.error, 'Close');
      }
    );
  }

  private openSnackBar(message: string, action: string): void {
    const config = new MatSnackBarConfig();
    config.horizontalPosition = 'center'; // Set horizontal position to center
    config.duration = 5000; // Duration in milliseconds

    this.snackBar.open(message, action, config);
  }

}
