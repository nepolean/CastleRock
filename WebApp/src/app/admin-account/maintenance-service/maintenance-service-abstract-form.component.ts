import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { MaintenanceService } from './maintenance-service';
import { MaintenanceServiceService } from './mock-maintenance-service.service';

@Component({
  moduleId: module.id,
  template: ''
})
export class MaintenanceServiceAbstractFormComponent extends AbstractFormComponent {

    protected readonly numberOfSteps: number = 3;
    protected currentStep: number = 1;

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected maintenanceServiceService: MaintenanceServiceService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    protected editMaintenanceService(maintenanceService: MaintenanceService): void {
        this.router.navigate(['/admin-dashboard/services/edit', maintenanceService.id]);
    }

    protected deleteMaintenanceService(maintenanceService: MaintenanceService): void {
        this.setLoadingState();
        this.maintenanceServiceService.deleteMaintenanceService(maintenanceService.id)
        .subscribe(
            response => this.handleDeletedSuccess(response),
            error => this.handleError(error)
        );
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'MaintenanceService deleted successfully';
        this.router.navigate(['/admin-dashboard/services']);
    }

    protected goToNext(): void {
         if(this.currentStep === this.numberOfSteps) {
            return;
         }
         this.currentStep++;
    }

    protected goToPrevious(): void {
         if(this.currentStep === 1) {
            return;
         }
         this.currentStep--;
    }

    protected getWizardStepCircleClass(step: number): string {
        if (this.currentStep === step) {
            return 'selected active';
        }
        if (this.currentStep > step) {
            return 'done';
        }
        return 'disabled';
    }

    protected getNextButtonClass(): string {
        if (this.currentStep === this.numberOfSteps) {
            return 'buttonDisabled btn btn-danger';
        }
        return 'buttonNext btn btn-danger';
    }

    protected getPreviousButtonClass(): string {
        if (this.currentStep === 1) {
            return 'buttonDisabled btn btn-danger';
        }
        return 'buttonPrevious btn btn-danger';
    }

    protected getFinishButtonClass(): string {
        if (this.currentStep === this.numberOfSteps) {
            return 'btn btn-default';
        }
        return 'buttonDisabled btn btn-default';
    }

    protected submitForm(): void {
        console.log('Submitted');
    }
}
