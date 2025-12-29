import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-foundform',
  standalone: true,
  imports: [FormsModule, CommonModule, SidebarComponent, HttpClientModule],
  templateUrl: './foundform.component.html',
  styleUrls: ['./foundform.component.css']
})
export class FoundComponent {
  itemname = '';
  description = '';
  location = '';
  contact = '';
  email = '';
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;

  constructor(private router: Router, private http: HttpClient) {}

onSubmit(form: any) {
  const userJson = localStorage.getItem('user') ?? sessionStorage.getItem('user');
  const currentUser = userJson ? JSON.parse(userJson) : { usn: '' };
  this.submitFoundItem(form, currentUser);
}
isDirty = false;

onInputChange() {
  this.isDirty = true;
}

canDeactivate(): boolean {
  if (this.isDirty) {
    return confirm('You have unsaved changes. Leave anyway?');
  }
  return true;
}

  submitFoundItem(form: any, user: any) { // user passed from parent or fetched from user service
    if (form.invalid) {
      Swal.fire('⚠️ Fill all fields', 'Please complete the form', 'warning');
      return;
    }

    const formData = new FormData();
    formData.append('itemName', this.itemname);
    formData.append('description', this.description);
    formData.append('location', this.location);
    formData.append('contact', this.contact);
    formData.append('email', this.email);
    // Pass the whole user object or just user.usn if backend expects it
    formData.append('usn', user.usn);

    if (this.selectedFile) formData.append('image', this.selectedFile);

    this.http.post('http://localhost:8080/api/found', formData).subscribe({
      next: (res: any) => {
        Swal.fire({
          title: "✅ Report Submitted!",
          text: "We’ll help you match it real soon 🤝✨",
          icon: "success",
          confirmButtonText: "Okay"
        });
        form.reset();
        this.itemname = '';
        this.description = '';
        this.location = '';
        this.contact = '';
        this.email = '';
        this.selectedFile = null;
        this.imagePreview = null;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error:', err);
        Swal.fire({
          title: "❌ Submission Failed!",
          text: "Try again later.",
          icon: "error"
        });
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => this.imagePreview = reader.result;
      reader.readAsDataURL(file);
    }
  }
}
