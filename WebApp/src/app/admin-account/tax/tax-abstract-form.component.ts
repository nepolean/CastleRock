import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Tax } from './tax';
import { TaxService } from './mock-tax.service';

@Component({
  moduleId: module.id,
  template: ''
})
export class TaxAbstractFormComponent extends AbstractFormComponent {

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected taxService: TaxService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    protected editTax(tax: Tax): void {
        this.router.navigate(['/admin-dashboard/taxes/edit', tax.id]);
    }

    protected deleteTax(tax: Tax): void {
        this.setLoadingState();
        this.taxService.deleteTax(tax.id)
        .subscribe(
            response => this.handleDeletedSuccess(response),
            error => this.handleError(error)
        );
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Tax deleted successfully';
        this.router.navigate(['/admin-dashboard/taxes']);
    }
}
