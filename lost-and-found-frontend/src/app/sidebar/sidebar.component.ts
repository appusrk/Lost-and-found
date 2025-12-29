import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  constructor(private router: Router) {}
  Logout() {
  const confirmLogout = confirm('Are you sure you want to logout?');
  if (confirmLogout) {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}


}
