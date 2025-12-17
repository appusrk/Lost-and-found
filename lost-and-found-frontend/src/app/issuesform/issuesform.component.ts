import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { SidebarComponent } from '../sidebar/sidebar.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-issues',
  standalone: true,
  imports: [FormsModule, CommonModule, SidebarComponent],
  templateUrl: './issuesform.component.html',
  styleUrls: ['./issuesform.component.css']
})
export class IssuesformComponent {
  private router = inject(Router);
  private http = inject(HttpClient);

  showOther = false;
  dept = '';
  description = '';
  location = '';
  contact = '';
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;

  onSubmit(form: NgForm) {
    const userJson = localStorage.getItem('user') ?? sessionStorage.getItem('user');
    const currentUser = userJson ? JSON.parse(userJson) : { usn: '' };
    this.submitIssue(form, currentUser);
  }

  submitIssue(form: NgForm, user: any) {
    if (form.invalid) {
      Swal.fire('⚠️ Fill all fields', 'Please complete the form', 'warning');
      return;
    }

    const formData = new FormData();
    formData.append('issue_dept', this.dept);
    formData.append('description', this.description);
    formData.append('location', this.location);
    formData.append('usn', user.usn);
    if (this.selectedFile) formData.append('image', this.selectedFile);

    this.http.post('http://localhost:8080/api/issues', formData).subscribe({
      next: () => {
        Swal.fire("✅ Report Submitted!", "We’ll resolve it as soon as possible 🤝✨", "success");
        form.resetForm();
        this.dept = '';
        this.description = '';
        this.location = '';
        this.selectedFile = null;
        this.imagePreview = null;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error(err);
        Swal.fire("❌ Submission Failed!", "Try again later.", "error");
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }
}

