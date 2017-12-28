import { Component, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-admin-account-sidebar-section',
  templateUrl: 'admin-account-sidebar.component.html'
})
export class AdminAccountSideBarComponent {

  @Output() sideBarToggle = new EventEmitter<any>();
  constructor(protected router: Router){}
  private toggleSideBar(): void {
    this.sideBarToggle.emit();
  }

}