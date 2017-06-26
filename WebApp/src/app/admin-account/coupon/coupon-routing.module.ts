import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CouponComponent } from './coupon.component';
import { CouponAddComponent } from './coupon-add.component';
import { CouponDetailComponent } from './coupon-detail.component';
import { CouponEditComponent } from './coupon-edit.component';

const routes: Routes = [
  { path: '', component: CouponComponent },
  { path: 'add', component: CouponAddComponent },
  { path: 'edit/:id', component: CouponEditComponent },
  { path: 'view/:id', component: CouponDetailComponent }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class CouponRoutingModule {}