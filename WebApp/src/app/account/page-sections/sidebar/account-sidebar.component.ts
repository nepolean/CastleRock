import { Component, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-account-sidebar-section',
  templateUrl: 'account-sidebar.component.html'
})
export class AccountSideBarComponent {

  @Output() sideBarToggle = new EventEmitter<any>();
  constructor(protected router: Router){}
  private toggleSideBar(): void {
    this.sideBarToggle.emit();
  }

}