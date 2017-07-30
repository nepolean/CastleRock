import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceServiceAbstractFormComponent } from './maintenance-service-abstract-form.component';
import { MaintenanceService } from './maintenance-service';
import { SubscriptionPlan } from './service-data';
import { OneTimePlan } from './service-data';
import { MaintenanceServiceService } from './mock-maintenance-service.service';
import { MaintenanceServiceFieldsDirective } from './maintenance-service-fields-directive.component';
import { SubscriptionPlanFieldsDirective } from './subscription-plan-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'maintenance-service-edit',
  templateUrl: 'maintenance-service-edit.component.html'
})
export class MaintenanceServiceEditComponent extends MaintenanceServiceAbstractFormComponent implements OnInit {

    private maintenanceService: MaintenanceService = new MaintenanceService();
    private selectedSubscriptionPlan: SubscriptionPlan = new SubscriptionPlan();
    private subscriptionPlan: SubscriptionPlan = new SubscriptionPlan();
    private selectedOneTimePlan: OneTimePlan = new OneTimePlan();
    private oneTimePlan: OneTimePlan = new OneTimePlan();
    private editMode: boolean = true;

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected maintenanceServiceService: MaintenanceServiceService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, maintenanceServiceService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.maintenanceServiceService.getMaintenanceService(params['id']))
            .subscribe(
                response => this.handleSuccess(response),
                error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        this.maintenanceService = successResponse.json() as MaintenanceService;
    }

    private updateMaintenanceService(): void {
        this.setLoadingState();
        this.maintenanceServiceService.updateMaintenanceService(this.maintenanceService)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.updateMaintenanceService();
    }

    public handleUpdatedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.goToNext();
        //this.router.navigate(['/admin-dashboard/services/view', this.maintenanceService.id]);
    }

    private selectSubscriptionPlan(subscriptionPlan: SubscriptionPlan): void {
        this.selectedSubscriptionPlan = subscriptionPlan;
    }

    private editSubscriptionPlan(): void {
        this.selectedSubscriptionPlan.discount = this.selectedSubscriptionPlan.subscriptionPrice * this.selectedSubscriptionPlan.discountPct / 100;
    }

    private addSubscriptionPlan(): void {
        let newSubscriptionPlan = new SubscriptionPlan();
        newSubscriptionPlan.name = this.subscriptionPlan.name;
        newSubscriptionPlan.visitCount = this.subscriptionPlan.visitCount;
        newSubscriptionPlan.subscriptionPrice = this.subscriptionPlan.subscriptionPrice;
        newSubscriptionPlan.discountPct = this.subscriptionPlan.discountPct;
        newSubscriptionPlan.discount = newSubscriptionPlan.subscriptionPrice * newSubscriptionPlan.discountPct / 100;
        this.maintenanceService.subscriptionServiceData.subscriptionData.push(newSubscriptionPlan);
    }

    private deleteSubscriptionPlan(subscriptionPlan: any): void {
        this.maintenanceService.subscriptionServiceData.subscriptionData.splice(this.maintenanceService.subscriptionServiceData.subscriptionData.indexOf(this.selectedSubscriptionPlan),1);
    }

    private saveSubscriptionPlan(): void {
        this.updateSubscriptionPlan();
    }

    private updateSubscriptionPlan(): void {
        this.setLoadingState();
        this.maintenanceServiceService.updateSubscriptionPlan(this.maintenanceService.id, this.maintenanceService.subscriptionServiceData)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private selectOneTimePlan(oneTimePlan: OneTimePlan): void {
        this.selectedOneTimePlan = oneTimePlan;
    }

    private editOneTimePlan(): void {
    }

    private addOneTimePlan(): void {
        let newOneTimePlan = new OneTimePlan();
        newOneTimePlan.name = this.oneTimePlan.name;
        newOneTimePlan.price = this.oneTimePlan.price;
        this.maintenanceService.oneTimeServiceData.oneTimeData.push(newOneTimePlan);
    }

    private deleteOneTimePlan(oneTimePlan: any): void {
        this.maintenanceService.oneTimeServiceData.oneTimeData.splice(this.maintenanceService.oneTimeServiceData.oneTimeData.indexOf(this.selectedOneTimePlan),1);
    }

    private saveOneTimePlan(): void {
        this.updateOneTimePlan();
    }

    private updateOneTimePlan(): void {
        this.setLoadingState();
        this.maintenanceServiceService.updateOneTimePlan(this.maintenanceService.id, this.maintenanceService.oneTimeServiceData)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private goHome(): void {
        this.router.navigate(['/admin-dashboard/services']);
    }

}
