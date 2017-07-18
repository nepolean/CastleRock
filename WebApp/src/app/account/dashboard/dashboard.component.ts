import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-account-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  constructor(
      private router: Router) {
  }

  ngOnInit(): void {
  }

  private goToLink(link: any): void {
    this.router.navigate([link]);
  }

}