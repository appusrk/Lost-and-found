import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  editMode: boolean = false;
  user: any = {};

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Load ORIGINAL stored user
    const userData = localStorage.getItem('user');

    if (userData) {
      this.user = JSON.parse(userData);
    } else {
      // No user = redirect to login
      this.router.navigate(['/login']);
    }
  }

 logout() {
  const confirmLogout = confirm('Are you sure you want to logout?');
  if (confirmLogout) {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}


  openEdit() {
    this.editMode = true;
  }

  saveChanges() {
    
    localStorage.setItem('user', JSON.stringify(this.user));

    alert("Profile updated!");
    this.editMode = false;
  }
}
