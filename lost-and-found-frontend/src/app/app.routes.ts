import { Routes } from '@angular/router';
import { LandingComponent } from './landing/landing.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { LostComponent } from './lostform/lostform.component';
import { FoundComponent } from './foundform/foundform.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { IssuesformComponent } from './issuesform/issuesform.component';
import { IssuesComponent } from './dashboard/issues/issues.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { AdminpannelComponent } from './adminpannel/adminpannel.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: RegisterComponent },
  { path: 'lostform', component: LostComponent },
  { path: 'findform', component: FoundComponent },
  { path: 'forgotpassword',  component: ForgotPasswordComponent },
  { path: 'issuesform', component: IssuesformComponent},
  { path: 'adminpanel', component: AdminpannelComponent},

  {
  path: 'dashboard',
  component: DashboardComponent,
  children: [
    { path: 'user-details', loadComponent: () => import('./dashboard/user-details/user-details.component').then(m => m.UserDetailsComponent) },
    { path: 'lost-reports', loadComponent: () => import('./dashboard/lost-reports/lost-reports.component').then(m => m.LostReportsComponent) },
    { path: 'found-reports', loadComponent: () => import('./dashboard/found-reports/found-reports.component').then(m => m.FoundReportsComponent) },
    { path: 'issues', component: IssuesComponent },
    { path: 'match-history', loadComponent: () => import('./dashboard/match-history/match-history.component').then(m => m.MatchHistoryComponent) },
  ]
},

  { path: '**', redirectTo: 'dashboard' }
];
