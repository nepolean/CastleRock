import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceServiceAbstractFormComponent } from './maintenance-service-abstract-form.component';
import { MaintenanceService } from './maintenance-service';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { MaintenanceServicePaginatorComponent } from './maintenance-service-paginator.component';
import { MaintenanceServiceService } from './mock-maintenance-service.service';
import { MaintenanceServiceFieldsDirective } from './maintenance-service-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'maintenance-service-app',
  templateUrl: 'maintenance-service.component.html'
})
export class MaintenanceServiceComponent extends MaintenanceServiceAbstractFormComponent implements OnInit {

    private maintenanceServices: MaintenanceService[];
    private selectedMaintenanceService: MaintenanceService = new MaintenanceService();
    private paginator: Paginator = new Paginator();

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected maintenanceServiceService: MaintenanceServiceService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, maintenanceServiceService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.getMaintenanceServices();
    }

    private handlePageSelection(paginator: Paginator): void {
        this.getMaintenanceServices();
    }

    private getMaintenanceServices(): void {
        this.maintenanceServiceService.getMaintenanceServices({
            page: this.paginator.currentPage
        }).subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    private searchMaintenanceService(name: string): void {
        this.maintenanceServiceService.getMaintenanceServicesByName(name)
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        const response: any = successResponse.json();
        this.maintenanceServices = response.content as MaintenanceService[];
        this.paginator.totalPages = response.totalPages;
        this.paginator.totalElements = response.totalElements;
        this.paginator.last = response.last;
        this.paginator.first = response.first;
        this.paginator.size = response.size;
        this.paginator.currentPage = response.number;
        this.paginator.numberOfElements = response.numberOfElements;
        this.paginator.entity = 'services';
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Maintenance service deleted successfully';
        this.maintenanceServices.splice(this.maintenanceServices.indexOf(this.selectedMaintenanceService), 1);
    }

    private selectMaintenanceService(maintenanceService: MaintenanceService): void {
        this.selectedMaintenanceService = maintenanceService;
    }
}
