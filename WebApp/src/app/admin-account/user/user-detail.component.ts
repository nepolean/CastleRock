import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { UserAbstractFormComponent } from './user-abstract-form.component';
import { UserActionButtonsDirective } from './user-action-buttons-directive.component';
import { UserActionModalsDirective } from './user-action-modals-directive.component';
import { User } from './user';
import { UserService } from './user.service';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'user-detail',
  templateUrl: 'user-detail.component.html'
})
export class UserDetailComponent extends UserAbstractFormComponent implements OnInit {

    private user: User = new User();

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
}