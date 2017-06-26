import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Profile } from '../../models/profile';
import { ProfileService } from '../../services/profile.service';

@Component({
  moduleId: module.id,
  selector: 'header-section',
  templateUrl: 'header.component.html'
})
export class HeaderComponent implements OnInit {

  private profile: Profile = new Profile("");
  constructor(
      private router: Router,
      private profileService: ProfileService) {
  }

  ngOnInit(): void {
    this.profileService.getProfile()
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
    );
  }

  gotoLink(link: any): void {
      this.router.navigate(link);
  }

  public handleSuccess(successResponse: any): void {
    this.profile = successResponse.json() as Profile;
  }

  public handleError(error: any): void {}
}