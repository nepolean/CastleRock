import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { UserAbstractFormComponent } from './user-abstract-form.component';
import { User } from './user';
import { UserService } from './user.service';
import { UserProfileFieldsDirective } from './user-profile-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'user-edit',
  templateUrl: 'user-edit.component.html'
})
export class UserEditComponent extends UserAbstractFormComponent implements OnInit {

    private user: User = new User();

    private editMode:boolean = true;

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected userService: UserService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router,route,userService,FormValidationMessageService);
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.userService.getUser(params['username']))
            .subscribe(
                response => this.handleSuccess(response),
                error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        this.user = successResponse.json() as User;
    }

    private updateUser(): void {
        this.setLoadingState();
        this.userService.updateUser(this.user)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private onSubmit():void {
        this.updateUser();
    }

    public handleUpdatedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.router.navigate(['/admin-dashboard/users/view', this.user.username]);
    }
}