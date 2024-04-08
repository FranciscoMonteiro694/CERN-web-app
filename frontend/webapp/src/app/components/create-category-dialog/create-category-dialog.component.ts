import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';

import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-create-category-dialog',
  standalone: true,
  imports: [MatDialogModule, FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './create-category-dialog.component.html',
  styleUrl: './create-category-dialog.component.css'
})
export class CreateCategoryDialogComponent {

  categoryName: string = '';
  categoryDescription: string = '';

  constructor(public dialogRef: MatDialogRef<CreateCategoryDialogComponent>) {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close({ name: this.categoryName, description: this.categoryDescription });
  }

  isValid(): boolean {
    return this.categoryName.trim() !== '' && this.categoryDescription.trim() !== '';
  }
}
