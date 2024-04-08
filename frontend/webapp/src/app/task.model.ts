import { TaskCategory } from "./task-category.model";

export interface Task {
    taskId: number;
    taskName: string;
    taskDescription: string;
    deadline: Date;
    categoryId?: number;
    category: TaskCategory;
  }
  