import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceService } from './maintenance-service';
import { SubscriptionPlan } from './service-data';

@Component({
  moduleId: module.id,
  selector: 'subscription-plan-fields',
  templateUrl: 'subscription-plan-fields-directive.html'
})

export class SubscriptionPlanFieldsDirective {

    @Input() subscriptionPlan: SubscriptionPlan;
    @Input() editMode: boolean;

    constructor(private FormValidationMessageService: FormValidationMessageService) {

    }

}