import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { PackageAbstractFormComponent } from './package-abstract-form.component';
import { Package } from './package';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { PackagePaginatorComponent } from './package-paginator.component';
import { PackageService } from './mock-package.service';
import { PackageFieldsDirective } from './package-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'package-app',
  templateUrl: 'package.component.html'
})
export class PackageComponent extends PackageAbstractFormComponent implements OnInit {

    private packages: Package[];
    private selectedPackage: Package = new Package();
    private paginator: Paginator = new Paginator();

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected packageService: PackageService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, packageService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.getPackages();
    }

    private handlePageSelection(paginator: Paginator): void {
        this.getPackages();
    }

    private getPackages(): void {
        this.packageService.getPackages({
            page: this.paginator.currentPage
        }).subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    private searchPackage(type: string): void {
        this.packageService.getPackagesByType(type)
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        const response: any = successResponse.json();
        this.packages = response.content as Package[];
        this.paginator.totalPages = response.totalPages;
        this.paginator.totalElements = response.totalElements;
        this.paginator.last = response.last;
        this.paginator.first = response.first;
        this.paginator.size = response.size;
        this.paginator.currentPage = response.number;
        this.paginator.numberOfElements = response.numberOfElements;
        this.paginator.entity = 'packages';
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Package deleted successfully';
        this.packages.splice(this.packages.indexOf(this.selectedPackage), 1);
    }

    private selectPackage(amcPackage: Package): void {
        this.selectedPackage = amcPackage;
    }
}
