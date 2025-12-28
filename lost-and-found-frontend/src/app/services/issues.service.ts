import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IssuesService {

  private baseUrl = 'http://localhost:8080/api/issues';

  constructor(private http: HttpClient) {}

  // 🔹 Create Issue
  createIssue(formData: FormData): Observable<any> {
    return this.http.post(this.baseUrl, formData);
  }

  // 🔹 Get ALL issues (ADMIN – fallback)
  getAllIssues(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // 🔹 Get issues by USER (normal user)
  getIssuesByUser(usn: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/user/${usn}`);
  }

  // 🔹 Get issues by DEPARTMENT (ADMIN)
  getIssuesByDept(dept: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/department/${dept}`);
  }

  // 🔹 Update issue status (ADMIN)
  updateIssueStatus(id: number, status: string): Observable<any> {
    return this.http.put(
      `${this.baseUrl}/${id}/status?status=${status}`,
      {}
    );
  }

  // 🔹 Delete issue
  deleteIssue(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
