import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { PackageAbstractFormComponent } from './package-abstract-form.component';
import { PackageActionButtonsDirective } from './package-action-buttons-directive.component';
import { PackageActionModalsDirective } from './package-action-modals-directive.component';
import { Package } from './package';
import { PackageService } from './mock-package.service';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'package-detail',
  templateUrl: 'package-detail.component.html'
})
export class PackageDetailComponent extends PackageAbstractFormComponent implements OnInit {

    private amcPackage: Package = new Package();

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
}
