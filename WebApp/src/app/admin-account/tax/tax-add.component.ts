import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Tax } from './tax';
import { TaxService } from './mock-tax.service';
import { TaxFieldsDirective } from './tax-fields-directive.component';

@Component({
  moduleId: module.id,
  selector: 'tax-add',
  templateUrl: 'tax-add.component.html'
})
export class TaxAddComponent extends AbstractFormComponent {

    private tax: Tax = new Tax();

    private editMode: boolean = false;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private taxService: TaxService,
        private FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    private addTax(routeToViewTax: boolean = false): void {
        this.setLoadingState();
        this.taxService.createTax(this.tax)
        .subscribe(
            response => this.handleSuccess(response, routeToViewTax),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.addTax(true);
    }

    public handleSuccess(successResponse: any, routeToViewTax: boolean): void {
        super.handleSuccess(successResponse);
        const response = successResponse.json();
        if (routeToViewTax) {
            this.router.navigate(['/admin-dashboard/taxes/view', 11]);
        } else {
            this.response.message = response.id + ' saved successfully';
        }

    }
}
