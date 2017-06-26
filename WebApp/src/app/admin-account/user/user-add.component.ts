import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { User } from './user';
import { UserService } from './user.service';
import { UserProfileFieldsDirective } from './user-profile-fields-directive.component';

@Component({
  moduleId: module.id,
  selector: 'user-add',
  templateUrl: 'user-add.component.html'
})
export class UserAddComponent extends AbstractFormComponent {

    private user: User = new User();

    private editMode:boolean = false;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private userService: UserService,
        private FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    private addUser(routeToViewUser :boolean = false): void {
        this.setLoadingState();
        this.userService.createUser(this.user)
        .subscribe(
            response => this.handleSuccess(response, routeToViewUser),
            error => this.handleError(error)
        );
    }

    private onSubmit():void {
        this.addUser(true);
    }

    public handleSuccess(successResponse: any, routeToViewUser :boolean): void {
        super.handleSuccess(successResponse);
        var response = successResponse.json();
        if(routeToViewUser)
            this.router.navigate(['/admin-dashboard/users/view', this.user.username]);
        else
            this.response.message = response.username + " saved successfully";

    }
}