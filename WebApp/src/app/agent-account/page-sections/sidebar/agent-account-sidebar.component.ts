import { Component, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-agent-account-sidebar-section',
  templateUrl: 'agent-account-sidebar.component.html'
})
export class AgentAccountSideBarComponent {

  @Output() sideBarToggle = new EventEmitter<any>();
  constructor(protected router: Router){}
  private toggleSideBar(): void {
    this.sideBarToggle.emit();
  }

}