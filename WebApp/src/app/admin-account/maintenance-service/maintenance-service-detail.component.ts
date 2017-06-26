import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceServiceAbstractFormComponent } from './maintenance-service-abstract-form.component';
import { MaintenanceServiceActionButtonsDirective } from './maintenance-service-action-buttons-directive.component';
import { MaintenanceServiceActionModalsDirective } from './maintenance-service-action-modals-directive.component';
import { MaintenanceService } from './maintenance-service';
import { MaintenanceServiceService } from './mock-maintenance-service.service';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'maintenance-service-detail',
  templateUrl: 'maintenance-service-detail.component.html'
})
export class MaintenanceServiceDetailComponent extends MaintenanceServiceAbstractFormComponent implements OnInit {

    private maintenanceService: MaintenanceService = new MaintenanceService();

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
}
