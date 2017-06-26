import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { MaintenanceService } from './maintenance-service';
import { MaintenanceServiceService } from './mock-maintenance-service.service';
import { MaintenanceServiceFieldsDirective } from './maintenance-service-fields-directive.component';

@Component({
  moduleId: module.id,
  selector: 'maintenance-service-add',
  templateUrl: 'maintenance-service-add.component.html'
})
export class MaintenanceServiceAddComponent extends AbstractFormComponent {

    private maintenanceService: MaintenanceService = new MaintenanceService();

    private editMode: boolean = false;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private maintenanceServiceService: MaintenanceServiceService,
        private FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    private addMaintenanceService(routeToViewMaintenanceService: boolean = false): void {
        this.setLoadingState();
        this.maintenanceServiceService.createMaintenanceService(this.maintenanceService)
        .subscribe(
            response => this.handleSuccess(response, routeToViewMaintenanceService),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.addMaintenanceService(true);
    }

    public handleSuccess(successResponse: any, routeToViewMaintenanceService: boolean): void {
        super.handleSuccess(successResponse);
        const response = successResponse.json();
        if (routeToViewMaintenanceService) {
            this.router.navigate(['/admin-dashboard/services/view', 11]);
        } else {
            this.response.message = response.id + ' saved successfully';
        }

    }
}
