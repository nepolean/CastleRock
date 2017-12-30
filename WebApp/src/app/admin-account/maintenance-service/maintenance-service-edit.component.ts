import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceServiceAbstractFormComponent } from './maintenance-service-abstract-form.component';
import { MaintenanceService } from './maintenance-service';
import { SubscriptionPlan } from './service-data';
import { OneTimePlan } from './service-data';
import { RealMaintenanceServiceService } from './maintenance-service.service';
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
  private ratings: string[] = ['ONE','TWO','THREE','FOUR','FIVE'];
  private ratingSelected: string = 'ONE';

  constructor(
    protected router: Router,
    protected route: ActivatedRoute,
    protected maintenanceServiceService: RealMaintenanceServiceService,
    protected FormValidationMessageService: FormValidationMessageService) {
    super(router, route, maintenanceServiceService, FormValidationMessageService);
  }

  ngOnInit(): void {
    console.log("In edit ngOnInit service ", this.maintenanceService);
    this.route.params
      .switchMap((params: Params) => this.maintenanceServiceService.getMaintenanceService(params['id']))
      .subscribe(
      response => this.handleSuccess(response),
      error => this.handleError(error)
      );
  }

  public handleSuccess(successResponse: any): void {
    this.maintenanceService = successResponse.json() as MaintenanceService;
    console.log('Maintenance Service', this.maintenanceService);
    if (this.maintenanceService.subscriptionServiceData == null) {
      this.maintenanceService.subscriptionServiceData = {};
      this.maintenanceService.subscriptionServiceData.subscriptionData = [];
    }
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
   // this.selectedSubscriptionPlan.discount = this.selectedSubscriptionPlan.subscriptionPrice * this.selectedSubscriptionPlan.discountPct / 100;
  }

  private subscriptionPlanAsArray(): SubscriptionPlan[] {
    console.log('getSubscriptionDetailsAsArray', this.maintenanceService.subscriptionServiceData.serviceData);
    console.log('isMap ', (this.maintenanceService.subscriptionServiceData.serviceData instanceof Map));
    if(this.maintenanceService.subscriptionServiceData.serviceData instanceof Map) {
      let plans = new Array<SubscriptionPlan>();
      this.maintenanceService.subscriptionServiceData.serviceData.elements().forEach(element => {
        let plan = new SubscriptionPlan();
        plan.name = element.name;
        plan.visitCount = element.visitCount;
        plan.subscriptionPrice = element.subscriptionPrice;
        plan.discountPct = element.discountPct;
        plans.push(plan);
        return plans;

      });;
    }
    return this.maintenanceService.subscriptionServiceData.serviceData;
  }

  private addSubscriptionPlan(): void {
    let newSubscriptionPlan = new SubscriptionPlan();
    newSubscriptionPlan.name = this.subscriptionPlan.name;
    newSubscriptionPlan.visitCount = this.subscriptionPlan.visitCount;
    newSubscriptionPlan.subscriptionPrice = this.subscriptionPlan.subscriptionPrice;
    newSubscriptionPlan.discountPct = this.subscriptionPlan.discountPct;
    this.ratings.splice(this.ratings.indexOf(this.subscriptionPlan.name),1);
    //newSubscriptionPlan.discount = newSubscriptionPlan.subscriptionPrice * newSubscriptionPlan.discountPct / 100;
    this.maintenanceService.subscriptionServiceData.subscriptionData.push(newSubscriptionPlan);
    console.log('after adding subscription plan', this.maintenanceService);
  }

  private deleteSubscriptionPlan(subscriptionPlan: any): void {
    this.maintenanceService.subscriptionServiceData.subscriptionData.delete(this.ratingSelected);
    this.ratings.push(this.ratingSelected);
  }

  private saveSubscriptionPlan(): void {
    this.updateSubscriptionPlan();
  }

  private updateSubscriptionPlan(): void {
    this.setLoadingState();
    this.maintenanceServiceService.updateSubscriptionPlan(this.maintenanceService.id, this.maintenanceService.subscriptionServiceData.subscriptionData)
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
    this.maintenanceService.oneTimeServiceData.oneTimeData.splice(this.maintenanceService.oneTimeServiceData.oneTimeData.indexOf(this.selectedOneTimePlan), 1);
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
