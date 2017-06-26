import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceServiceAbstractFormComponent } from './maintenance-service-abstract-form.component';
import { MaintenanceService } from './maintenance-service';
import { MaintenanceServiceService } from './mock-maintenance-service.service';
import { MaintenanceServiceFieldsDirective } from './maintenance-service-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'maintenance-service-edit',
  templateUrl: 'maintenance-service-edit.component.html'
})
export class MaintenanceServiceEditComponent extends MaintenanceServiceAbstractFormComponent implements OnInit {

    private maintenanceService: MaintenanceService = new MaintenanceService();
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
        this.router.navigate(['/admin-dashboard/services/view', this.maintenanceService.id]);
    }
}
