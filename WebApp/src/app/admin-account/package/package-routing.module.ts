import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PackageComponent } from './package.component';
import { PackageAddComponent } from './package-add.component';
import { PackageDetailComponent } from './package-detail.component';
import { PackageEditComponent } from './package-edit.component';

const routes: Routes = [
  { path: '', component: PackageComponent },
  { path: 'add', component: PackageAddComponent },
  { path: 'edit/:id', component: PackageEditComponent },
  { path: 'view/:id', component: PackageDetailComponent }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class PackageRoutingModule {}