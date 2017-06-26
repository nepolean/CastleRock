import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { TaxAbstractFormComponent } from './tax-abstract-form.component';
import { Tax } from './tax';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { TaxPaginatorComponent } from './tax-paginator.component';
import { TaxService } from './mock-tax.service';
import { TaxFieldsDirective } from './tax-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'tax-app',
  templateUrl: 'tax.component.html'
})
export class TaxComponent extends TaxAbstractFormComponent implements OnInit {

    private taxes: Tax[];
    private selectedTax: Tax = new Tax();
    private paginator: Paginator = new Paginator();

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected taxService: TaxService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, taxService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.getTaxes();
    }

    private handlePageSelection(paginator: Paginator): void {
        this.getTaxes();
    }

    private getTaxes(): void {
        this.taxService.getTaxes({
            page: this.paginator.currentPage
        }).subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    private searchTax(type: string): void {
        this.taxService.getTaxesByType(type)
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        const response: any = successResponse.json();
        this.taxes = response.content as Tax[];
        this.paginator.totalPages = response.totalPages;
        this.paginator.totalElements = response.totalElements;
        this.paginator.last = response.last;
        this.paginator.first = response.first;
        this.paginator.size = response.size;
        this.paginator.currentPage = response.number;
        this.paginator.numberOfElements = response.numberOfElements;
        this.paginator.entity = 'taxes';
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Tax deleted successfully';
        this.taxes.splice(this.taxes.indexOf(this.selectedTax), 1);
    }

    private selectTax(tax: Tax): void {
        this.selectedTax = tax;
    }
}
