import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Package } from './package';
import { PackageService } from './mock-package.service';

@Component({
  moduleId: module.id,
  template: ''
})
export class PackageAbstractFormComponent extends AbstractFormComponent {

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected packageService: PackageService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    protected editPackage(amcPackage: Package): void {
        this.router.navigate(['/admin-dashboard/packages/edit', amcPackage.id]);
    }

    protected deletePackage(amcPackage: Package): void {
        this.setLoadingState();
        this.packageService.deletePackage(amcPackage.id)
        .subscribe(
            response => this.handleDeletedSuccess(response),
            error => this.handleError(error)
        );
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Package deleted successfully';
        this.router.navigate(['/admin-dashboard/packages']);
    }
}
