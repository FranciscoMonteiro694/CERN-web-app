import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TaskCategory } from '../../task-category.model';

@Component({
  selector: 'app-create-task-dialog',
  standalone: true,
  imports: [MatDialogModule, FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, CommonModule],
  templateUrl: './create-task-dialog.component.html',
  styleUrl: './create-task-dialog.component.css'
})
export class CreateTaskDialogComponent {

  taskName: string = '';
  taskDescription: string = '';
  deadline: string = '';
  categoryId: number = 0;

  constructor(
    public dialogRef: MatDialogRef<CreateTaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { categories: TaskCategory[] } 
  ) { }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    const selectedCategory = this.data.categories.find(category => category.categoryId == this.categoryId);

    const categoryId = selectedCategory ? selectedCategory.categoryId : '';
    const deadline = this.formatDateTime(this.deadline);

    this.dialogRef.close({ taskName: this.taskName, taskDescription: this.taskDescription, deadline: deadline, categoryId: categoryId });
  }

  private formatDateTime(dateTimeString: string): string {
    const date = new Date(dateTimeString);
    const formattedDateTimeString = `${date.toISOString().slice(0, 16)}:00`;
    return formattedDateTimeString;
  }

}
