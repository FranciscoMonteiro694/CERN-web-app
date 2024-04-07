import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskCategory } from './task-category.model';

@Injectable({
  providedIn: 'root'
})
export class CategoriesApiService {

  private baseUrl = 'http://localhost:8080/categories';

  constructor(private http: HttpClient) { }

  getAllCategories(): Observable<TaskCategory[]> {
    return this.http.get<TaskCategory[]>(this.baseUrl);
  }

  createCategory(category: TaskCategory): Observable<TaskCategory> {
    return this.http.post<TaskCategory>(this.baseUrl, category);
  }
}
