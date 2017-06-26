import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Profile } from '../../common/models/profile';
import { ProfileService } from '../../common/services/profile.service';

import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'app-user-profile-edit',
  templateUrl: 'user-profile-edit.component.html'
})
export class UserProfileEditComponent extends AbstractFormComponent implements OnInit {

    private profile: Profile = new Profile();

    constructor(
        private router: Router,
        private profileService: ProfileService,
        private FormValidationMessageService: FormValidationMessageService) {
            super();
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

    private updateProfile(): void {
        this.setLoadingState();
        this.profileService.updateProfile(this.profile)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.updateProfile();
    }

    public handleUpdatedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.profile = successResponse.json() as Profile;
        this.profileService.broadcastProfileUpdate(this.profile);
        this.response.message = 'Profile updated successfully';
    }
}