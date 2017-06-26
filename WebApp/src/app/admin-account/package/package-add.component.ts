import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Package } from './package';
import { PackageService } from './mock-package.service';
import { PackageFieldsDirective } from './package-fields-directive.component';

@Component({
  moduleId: module.id,
  selector: 'package-add',
  templateUrl: 'package-add.component.html'
})
export class PackageAddComponent extends AbstractFormComponent {

    private amcPackage: Package = new Package();

    private editMode: boolean = false;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private packageService: PackageService,
        private FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    private addPackage(routeToViewPackage: boolean = false): void {
        this.setLoadingState();
        this.packageService.createPackage(this.amcPackage)
        .subscribe(
            response => this.handleSuccess(response, routeToViewPackage),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.addPackage(true);
    }

    public handleSuccess(successResponse: any, routeToViewPackage: boolean): void {
        super.handleSuccess(successResponse);
        const response = successResponse.json();
        if (routeToViewPackage) {
            this.router.navigate(['/admin-dashboard/packages/view', 11]);
        } else {
            this.response.message = response.id + ' saved successfully';
        }

    }
}
