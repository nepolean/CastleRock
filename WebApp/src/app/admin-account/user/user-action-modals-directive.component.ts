import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { User } from './user';
import { UserService } from './user.service';

@Component({
  moduleId: module.id,
  selector: 'user-action-modals',
  templateUrl: 'user-action-modals-directive.html'
})
export class UserActionModalsDirective implements OnInit {

    constructor(private userService: UserService) {}
    @Input() user: User;
    @Output() disableUser: EventEmitter<User> = new EventEmitter();
    @Output() deleteUser: EventEmitter<User> = new EventEmitter();
    @Output() lockUser: EventEmitter<User> = new EventEmitter();
    @Output() setRoles: EventEmitter<any> = new EventEmitter();
    private selectedRoles: string[];
    private allAvailableRoles: any[];

    ngOnInit(): void {
      this.getAllAvailableRoles();
    }

    private getAllAvailableRoles(): void {
        this.userService.getAllAvailableRoles()
        .subscribe(
            response => this.handleGotRoleSuccess(response),
            error => console.log(error)
        );
    }

    private handleGotRoleSuccess(successResponse: any): void {
        this.allAvailableRoles = successResponse.json().content;
    }

    private disable() :void {
      this.disableUser.emit(this.user);
    }

    private delete() :void {
      this.deleteUser.emit(this.user);
    }

    private lock() :void {
      this.lockUser.emit(this.user);
    }

    private setTheRoles() :void {
      let userRole: any = {};
      userRole["user"] = this.user;
      userRole["selectedRoles"] = this.selectedRoles;
      this.setRoles.emit(userRole);
      this.selectedRoles = [];
    }
}