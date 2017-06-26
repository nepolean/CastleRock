import { Component, Input } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { Package } from './package';

@Component({
  moduleId: module.id,
  selector: 'package-fields',
  templateUrl: 'package-fields-directive.html'
})
export class PackageFieldsDirective {

    @Input() amcPackage: Package;
    @Input() editMode: boolean;

    constructor(private FormValidationMessageService: FormValidationMessageService) {}
}
