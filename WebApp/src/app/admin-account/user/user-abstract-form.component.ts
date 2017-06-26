import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { User } from './user';
import { UserService } from './user.service';

@Component({
  moduleId: module.id,
  template: ''
})
export class UserAbstractFormComponent extends AbstractFormComponent {

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected userService: UserService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    protected editUser(user: User): void {
        this.router.navigate(['/admin-dashboard/users/edit',user.username]);
    }

    protected disableUser(user: User): void {
        this.setLoadingState();
        this.userService.disableUser(user.username)
        .subscribe(
            response => this.handleDisabledSuccess(response, user),
            error => this.handleError(error)
        );
    }

    protected handleDisabledSuccess(successResponse: any, user: User): void {
        super.handleSuccess(successResponse);
        user.enabled = false;
        this.response.message = 'User disabled successfully';
    }

    protected enableUser(user: User): void {
        this.setLoadingState();
        this.userService.enableUser(user.username)
        .subscribe(
            response => this.handleEnabledSuccess(response, user),
            error => this.handleError(error)
        );
    }

    protected handleEnabledSuccess(successResponse: any, user: User): void {
        super.handleSuccess(successResponse);
        user.enabled = true;
        this.response.message = 'User enabled successfully';
    }

    protected lockUser(user: User): void {
        this.setLoadingState();
        this.userService.lockUser(user.username)
        .subscribe(
            response => this.handleLockedSuccess(response, user),
            error => this.handleError(error)
        );
    }

    protected handleLockedSuccess(successResponse: any, user: User): void {
        super.handleSuccess(successResponse);
        user.accountLocked = true;
        this.response.message = 'User locked successfully';
    }

    protected unlockUser(user: User): void {
        this.setLoadingState();
        this.userService.unlockUser(user.username)
        .subscribe(
            response => this.handleUnlockedSuccess(response, user),
            error => this.handleError(error)
        );
    }

    protected handleUnlockedSuccess(successResponse: any, user: User): void {
        super.handleSuccess(successResponse);
        user.accountLocked = false;
        this.response.message = 'User unlocked successfully';
    }

    protected sendPasswordResetLink(user: User): void {
        this.setLoadingState();
        this.userService.sendPasswordResetLink(user.username)
        .subscribe(
            response => this.handlePasswordSentSuccess(response),
            error => this.handleError(error)
        );
    }

    protected handlePasswordSentSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Password link sent successfully';
    }

    protected deleteUser(user: User): void {
        this.setLoadingState();
        this.userService.deleteUser(user.username)
        .subscribe(
            response => this.handleDeletedSuccess(response),
            error => this.handleError(error)
        );
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'User deleted successfully';
        this.router.navigate(['/admin-dashboard/users']);
    }

    protected setRoles(userRole: any): void {
        this.setLoadingState();
        this.userService.setRoles(userRole.user.username, userRole.selectedRoles)
        .subscribe(
            response => this.handleRoleSetSuccess(response, userRole.user, userRole.selectedRoles),
            error => this.handleError(error)
        );
    }

    protected handleRoleSetSuccess(successResponse: any, user: User, selectedRoles: string[]): void {
        super.handleSuccess(successResponse);
        this.response.message = 'User roles set successfully';
        user.roles = selectedRoles;
    }
}