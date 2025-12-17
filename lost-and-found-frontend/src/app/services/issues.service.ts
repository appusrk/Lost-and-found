import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class IssuesService {
  private baseUrl = 'http://localhost:8080/api/issues';

  constructor(private http: HttpClient) {}

  // Create an issue
  createIssue(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}`, formData);
  }

  // Get all issues
  getAllIssues(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // Delete issue by ID
  deleteIssue(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
