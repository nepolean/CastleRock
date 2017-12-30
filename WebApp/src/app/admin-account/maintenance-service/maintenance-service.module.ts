import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppCommonModule } from '../../common/app-common.module';
import { MaintenanceServiceRoutingModule } from './maintenance-service-routing.module';
import { MyDatePickerModule } from 'mydatepicker';
import { MyDateRangePickerModule } from 'mydaterangepicker';
import { MaintenanceServiceComponent } from './maintenance-service.component';
import { MaintenanceServiceAbstractFormComponent } from './maintenance-service-abstract-form.component';
import { MaintenanceServiceDetailComponent } from './maintenance-service-detail.component';
import { MaintenanceServiceFieldsDirective } from './maintenance-service-fields-directive.component';
import { MaintenanceServiceActionButtonsDirective } from './maintenance-service-action-buttons-directive.component';
import { MaintenanceServiceActionModalsDirective } from './maintenance-service-action-modals-directive.component';
import { MaintenanceServicePaginatorComponent } from './maintenance-service-paginator.component';
import { MaintenanceServiceEditComponent } from './maintenance-service-edit.component';
import { MaintenanceServiceAddComponent } from './maintenance-service-add.component';
import { MaintenanceServiceService } from './mock-maintenance-service.service';
import { RealMaintenanceServiceService } from 'app/admin-account/maintenance-service/maintenance-service.service';
import { TaxService } from 'app/admin-account/tax/tax.service'
import { TaxModule } from 'app/admin-account/tax/tax.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    MaintenanceServiceRoutingModule,
    MyDatePickerModule,
    MyDateRangePickerModule,
    TaxModule
  ],
  declarations: [
    MaintenanceServiceComponent,
    MaintenanceServiceAbstractFormComponent,
    MaintenanceServiceFieldsDirective,
    MaintenanceServiceActionButtonsDirective,
    MaintenanceServiceActionModalsDirective,
    MaintenanceServicePaginatorComponent,
    MaintenanceServiceDetailComponent,
    MaintenanceServiceAddComponent,
    MaintenanceServiceEditComponent
  ],
  providers: [
    MaintenanceServiceService,
    RealMaintenanceServiceService
  ]
})
export class MaintenanceServiceModule { }