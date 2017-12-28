import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Profile } from '../common/models/profile';
import { ProfileService } from '../common/services/profile.service';

@Component({
  moduleId: module.id,
  selector: 'app-agent-account',
  templateUrl: 'agent-account.component.html'
})
export class AgentAccountComponent {

  private sidebarClasses: {[id: string] : boolean} = {};

  constructor(
      private router: Router) {
      this.sidebarClasses['sidebar-active'] = false;
  }

  private handleSideBarToggle(obj: any): void {
    this.sidebarClasses['sidebar-active'] = !this.sidebarClasses['sidebar-active'];
  }

  private goToLink(link: any): void {
    this.router.navigate([link]);
  }

}