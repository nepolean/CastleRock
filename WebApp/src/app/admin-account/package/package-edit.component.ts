import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { PackageAbstractFormComponent } from './package-abstract-form.component';
import { Package } from './package';
import { PackageService } from './mock-package.service';
import { PackageFieldsDirective } from './package-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'package-edit',
  templateUrl: 'package-edit.component.html'
})
export class PackageEditComponent extends PackageAbstractFormComponent implements OnInit {

    private amcPackage: Package = new Package();

    private editMode: boolean = true;

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected packageService: PackageService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, packageService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.packageService.getPackage(params['id']))
            .subscribe(
                response => this.handleSuccess(response),
                error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        this.amcPackage = successResponse.json() as Package;
    }

    private updatePackage(): void {
        this.setLoadingState();
        this.packageService.updatePackage(this.amcPackage)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.updatePackage();
    }

    public handleUpdatedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.router.navigate(['/admin-dashboard/packages/view', this.amcPackage.id]);
    }
}
