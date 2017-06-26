import { Component, Input } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { Tax } from './tax';

@Component({
  moduleId: module.id,
  selector: 'tax-fields',
  templateUrl: 'tax-fields-directive.html'
})
export class TaxFieldsDirective {

    @Input() tax: Tax;
    @Input() editMode: boolean;

    constructor(private FormValidationMessageService: FormValidationMessageService) {}
}
