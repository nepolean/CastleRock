import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-account-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  constructor(
      private router: Router) {
  }

  ngOnInit(): void {
  }

  private goToLink(link: any): void {
    this.router.navigate([link]);
  }

}