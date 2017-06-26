import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { TaxAbstractFormComponent } from './tax-abstract-form.component';
import { TaxActionButtonsDirective } from './tax-action-buttons-directive.component';
import { TaxActionModalsDirective } from './tax-action-modals-directive.component';
import { Tax } from './tax';
import { TaxService } from './mock-tax.service';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'tax-detail',
  templateUrl: 'tax-detail.component.html'
})
export class TaxDetailComponent extends TaxAbstractFormComponent implements OnInit {

    private tax: Tax = new Tax();

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
}
