import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { TaxAbstractFormComponent } from './tax-abstract-form.component';
import { Tax } from './tax';
import { TaxService } from './mock-tax.service';
import { TaxFieldsDirective } from './tax-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'tax-edit',
  templateUrl: 'tax-edit.component.html'
})
export class TaxEditComponent extends TaxAbstractFormComponent implements OnInit {

    private tax: Tax = new Tax();

    private editMode: boolean = true;

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected taxService: TaxService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, taxService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.taxService.getTax(params['id']))
            .subscribe(
                response => this.handleSuccess(response),
                error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        this.tax = successResponse.json() as Tax;
    }

    private updateTax(): void {
        this.setLoadingState();
        this.taxService.updateTax(this.tax)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.updateTax();
    }

    public handleUpdatedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.router.navigate(['/admin-dashboard/taxes/view', this.tax.id]);
    }
}
