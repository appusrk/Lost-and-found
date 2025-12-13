import { Component } from '@angular/core';
import { HeaderComponent } from '../shared/header/header.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { RouterOutlet } from '@angular/router';

import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LoadingComponent } from '../shared/loading/loading.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, RouterOutlet, CommonModule, LoadingComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private router: Router) {}

   isAuthPage(): boolean {
    const current = this.router.url;
    
    return (
      current === '/' ||
      current.includes('login') ||
      current.includes('dashboard') ||
      current.includes('register')
    );
  }

}
