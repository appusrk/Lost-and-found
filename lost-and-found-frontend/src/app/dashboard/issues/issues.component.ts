import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { IssuesService } from '../../services/issues.service';

@Component({
  selector: 'app-issues',
  standalone: true,
  imports: [CommonModule, FormsModule, MatSnackBarModule],
  templateUrl: './issues.component.html',
  styleUrls: ['./issues.component.css']
})
export class IssuesComponent implements OnInit {

  private issueService = inject(IssuesService);
  private snackBar = inject(MatSnackBar);

  issues: any[] = [];
  isAdmin = false;
  userDept = '';

  ngOnInit(): void {
    const user = JSON.parse(localStorage.getItem('user') || '{}');

    this.isAdmin = user.userLevel?.trim().toLowerCase() === 'admin';
    this.userDept = user.department;

    if (this.isAdmin) {
      // ADMIN → only their department issues
      this.issueService.getAllIssues().subscribe(data => {
        this.issues = data.filter(
          issue => issue.issueDept?.toLowerCase() === this.userDept?.toLowerCase()
        );
      });
    } else {
      // USER → own issues
      this.issueService.getIssuesByUser(user.usn)
        .subscribe(data => this.issues = data);
    }
  }

  updateStatus(issue: any) {
    this.issueService.updateIssueStatus(issue.id, issue.status)
      .subscribe(() => {
        this.snackBar.open(
          'Status updated successfully ✅',
          'X',
          { duration: 3000, panelClass: ['success-snackbar'] }
        );
      });
  }

  getStatusClass(status: string) {
    switch (status?.toLowerCase()) {
      case 'open': return 'status-open';
      case 'in progress': return 'status-progress';
      case 'resolved': return 'status-resolved';
      default: return 'status-open';
    }
  }
}
