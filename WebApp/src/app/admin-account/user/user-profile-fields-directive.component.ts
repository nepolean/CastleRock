import { Component, Input } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { User } from './user';

@Component({
  moduleId: module.id,
  selector: 'user-profile-fields',
  templateUrl: 'user-profile-fields-directive.html'
})
export class UserProfileFieldsDirective {

    constructor(private FormValidationMessageService: FormValidationMessageService) {}
    @Input() user: User;
    @Input() editMode: boolean;

}