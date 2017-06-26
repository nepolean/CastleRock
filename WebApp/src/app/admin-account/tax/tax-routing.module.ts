import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TaxComponent } from './tax.component';
import { TaxAddComponent } from './tax-add.component';
import { TaxDetailComponent } from './tax-detail.component';
import { TaxEditComponent } from './tax-edit.component';

const routes: Routes = [
  { path: '', component: TaxComponent },
  { path: 'add', component: TaxAddComponent },
  { path: 'edit/:id', component: TaxEditComponent },
  { path: 'view/:id', component: TaxDetailComponent }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class TaxRoutingModule {}