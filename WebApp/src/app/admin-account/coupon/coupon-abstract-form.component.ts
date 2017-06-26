import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Coupon } from './coupon';
import { CouponService } from './mock-coupon.service';

@Component({
  moduleId: module.id,
  template: ''
})
export class CouponAbstractFormComponent extends AbstractFormComponent {

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected couponService: CouponService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    protected editCoupon(coupon: Coupon): void {
        this.router.navigate(['/admin-dashboard/coupons/edit', coupon.id]);
    }

    protected deleteCoupon(coupon: Coupon): void {
        this.setLoadingState();
        this.couponService.deleteCoupon(coupon.id)
        .subscribe(
            response => this.handleDeletedSuccess(response),
            error => this.handleError(error)
        );
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Coupon deleted successfully';
        this.router.navigate(['/admin-dashboard/coupons']);
    }
}
