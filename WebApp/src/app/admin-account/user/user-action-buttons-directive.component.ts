import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { User } from './user';

@Component({
  moduleId: module.id,
  selector: 'user-action-buttons',
  templateUrl: 'user-action-buttons-directive.html'
})
export class UserActionButtonsDirective {

    constructor() {}
    @Input() user: User;
    @Input() listPage: boolean = false;
    @Output() editUser: EventEmitter<User> = new EventEmitter();
    @Output() enableUser: EventEmitter<User> = new EventEmitter();
    @Output() unlockUser: EventEmitter<User> = new EventEmitter();
    @Output() sendPasswordResetLink: EventEmitter<User> = new EventEmitter();

    private edit() :void {
      this.editUser.emit(this.user);
    }

    private enable() :void {
      this.enableUser.emit(this.user);
    }

    private unlock() :void {
      this.unlockUser.emit(this.user);
    }

    private sendPasswordReset() :void {
      this.sendPasswordResetLink.emit(this.user);
    }
}