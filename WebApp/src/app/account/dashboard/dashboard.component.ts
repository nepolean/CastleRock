import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-account-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private propertyName:string='';
  constructor(
      private router: Router) {
  }

  ngOnInit(): void {
    let propName=localStorage.getItem('appType');
    if(propName==null || propName==undefined)
    {
      localStorage.setItem('appType','Purva Supreme');
    }
  }

  private goToLink(link: any): void {
    this.router.navigate([link]);
  }

  private chooseProerty(name: any): void {
    this.propertyName=name;
    localStorage.setItem('appType',name);
    this.router.navigate(['/dashboard/service',name]);
  }
}