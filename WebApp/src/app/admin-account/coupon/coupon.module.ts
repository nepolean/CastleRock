import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppCommonModule } from '../../common/app-common.module';
import { CouponRoutingModule } from './coupon-routing.module';
import { MyDatePickerModule } from 'mydatepicker';
import { MyDateRangePickerModule } from 'mydaterangepicker';
import { CouponComponent } from './coupon.component';
import { CouponAbstractFormComponent } from './coupon-abstract-form.component';
import { CouponDetailComponent } from './coupon-detail.component';
import { CouponFieldsDirective } from './coupon-fields-directive.component';
import { CouponActionButtonsDirective } from './coupon-action-buttons-directive.component';
import { CouponActionModalsDirective } from './coupon-action-modals-directive.component';
import { CouponPaginatorComponent } from './coupon-paginator.component';
import { CouponEditComponent } from './coupon-edit.component';
import { CouponAddComponent } from './coupon-add.component';
import { CouponService } from './mock-coupon.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    CouponRoutingModule,
    MyDatePickerModule,
    MyDateRangePickerModule
  ],
  declarations: [
    CouponComponent,
    CouponAbstractFormComponent,
    CouponFieldsDirective,
    CouponActionButtonsDirective,
    CouponActionModalsDirective,
    CouponPaginatorComponent,
    CouponDetailComponent,
    CouponAddComponent,
    CouponEditComponent
  ],
  providers: [
    CouponService
  ]
})
export class CouponModule { }