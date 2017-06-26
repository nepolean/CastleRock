import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MaintenanceServiceComponent } from './maintenance-service.component';
import { MaintenanceServiceDetailComponent } from './maintenance-service-detail.component';
import { MaintenanceServiceAddComponent } from './maintenance-service-add.component';
import { MaintenanceServiceEditComponent } from './maintenance-service-edit.component';

const routes: Routes = [
  { path: '', component: MaintenanceServiceComponent },
  { path: 'add', component: MaintenanceServiceAddComponent },
  { path: 'edit/:id', component: MaintenanceServiceEditComponent },
  { path: 'view/:id', component: MaintenanceServiceDetailComponent }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class MaintenanceServiceRoutingModule {}
