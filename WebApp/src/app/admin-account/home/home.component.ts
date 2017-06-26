import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Profile } from '../../common/models/profile';
import { ProfileService } from '../../common/services/profile.service';

@Component({
  moduleId: module.id,
  selector: 'app-admin-account-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  private profile: Profile = new Profile('');

  constructor(
      private router: Router,
      private profileService: ProfileService) {
      this.profileService.profileUpdate.subscribe(profile => this.handleProfileUpdate(profile));
  }

  ngOnInit(): void {
    this.profileService.getProfile()
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
    );
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