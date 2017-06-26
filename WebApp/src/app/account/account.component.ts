import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Profile } from '../common/models/profile';
import { ProfileService } from '../common/services/profile.service';

@Component({
  moduleId: module.id,
  selector: 'app-account',
  templateUrl: 'account.component.html'
})
export class AccountComponent implements OnInit {

  private sidebarClasses: {[id: string] : boolean} = {};
  private profile: Profile = new Profile('');

  constructor(
      private router: Router,
      private profileService: ProfileService) {
      this.sidebarClasses['sidebar-active'] = false;
      this.profileService.profileUpdate.subscribe(profile => this.handleProfileUpdate(profile));
  }

  ngOnInit(): void {
    this.profileService.getProfile()
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
    );
  }

  private handleSideBarToggle(obj: any): void {
    this.sidebarClasses['sidebar-active'] = !this.sidebarClasses['sidebar-active'];
  }

  public handleSuccess(successResponse: any): void {
    this.profile = successResponse.json() as Profile;
  }

  public handleError(error: any): void {}

  public handleProfileUpdate(profile: Profile): void {
    this.profile = profile;
  }

  private goToLink(link: any): void {
    this.router.navigate([link]);
  }

}