import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Profile } from '../../common/models/profile';
import { ProfileService } from '../../common/services/profile.service';
import { PasswordDTO } from './passwordDTO';

import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'app-user-profile-change-password',
  templateUrl: 'user-profile-change-password.component.html'
})
export class UserProfileChangePasswordComponent extends AbstractFormComponent {

    private passwordDTO: PasswordDTO = new PasswordDTO();

    constructor(
        private router: Router,
        private profileService: ProfileService,
        private FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    private updatePassword(): void {
        this.setLoadingState();
        this.profileService.updatePassword({
            'password': this.passwordDTO.password,
            'oldPassword': this.passwordDTO.oldPassword,
            'verifyPassword': this.passwordDTO.verifyPassword
        })
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.updatePassword();
    }

    public handleUpdatedSuccess(successResponse: any): void {
        this.response.message = 'Password changed successfully';
        super.handleSuccess(successResponse);
    }
}