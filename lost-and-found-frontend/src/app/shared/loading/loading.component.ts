import { Component, OnInit } from '@angular/core';
import { LoadingService } from '../../services/loading.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.css']
})
export class LoadingComponent implements OnInit {

  isLoading = false;

  constructor(private loadingService: LoadingService) {}

  ngOnInit() {
    this.loadingService.isLoading$.subscribe(v => {
      this.isLoading = v;
    });
  }
}
