import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { UserAbstractFormComponent } from './user-abstract-form.component';
import { User } from './user';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { UserPaginatorComponent } from './user-paginator.component';
import { UserService } from './user.service';
import { UserProfileFieldsDirective } from './user-profile-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'user-app',
  templateUrl: 'user.component.html'
})
export class UserComponent extends UserAbstractFormComponent implements OnInit {

    private users: User[];
    private selectedUser: User = new User();
    private paginator: Paginator = new Paginator();
    private selectedType: string = 'ALL';

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected userService: UserService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router,route,userService,FormValidationMessageService);
    }

    ngOnInit(): void {
        this.getUsersByType();
    }

    private handlePageSelection(paginator: Paginator): void {
        this.getUsersByType();
    }

    private getUsersByType(): void {
        this.userService.getUsersByType({
            page: this.paginator.currentPage,
            type: this.selectedType
        })
            .subscribe(
                response => this.handleSuccess(response),
                error => this.handleError(error)
        );
    }

    private searchUser(username: string): void {
        this.userService.getUsersByUsername(username)
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        let response: any = successResponse.json();
        this.users = response.content as User[];
        this.paginator.totalPages = response.totalPages;
        this.paginator.totalElements = response.totalElements;
        this.paginator.last = response.last;
        this.paginator.first = response.first;
        this.paginator.size = response.size;
        this.paginator.currentPage = response.number;
        this.paginator.numberOfElements = response.numberOfElements;
        this.paginator.entity = 'users';
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'User deleted successfully';
        this.users.splice(this.users.indexOf(this.selectedUser),1);
    }

    private selectUser(user: User): void {
        this.selectedUser = user;
    }
}