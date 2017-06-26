import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { AbstractFormComponent } from '../../common/page-utils/form/abstract-form.component';
import { Coupon } from './coupon';
import { CouponService } from './mock-coupon.service';
import { CouponFieldsDirective } from './coupon-fields-directive.component';

@Component({
  moduleId: module.id,
  selector: 'coupon-add',
  templateUrl: 'coupon-add.component.html'
})
export class CouponAddComponent extends AbstractFormComponent {

    private coupon: Coupon = new Coupon();

    private editMode: boolean = false;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private couponService: CouponService,
        private FormValidationMessageService: FormValidationMessageService) {
            super();
    }

    private addCoupon(routeToViewCoupon: boolean = false): void {
        this.setLoadingState();
        this.couponService.createCoupon(this.coupon)
        .subscribe(
            response => this.handleSuccess(response, routeToViewCoupon),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.addCoupon(true);
    }

    public handleSuccess(successResponse: any, routeToViewCoupon: boolean): void {
        super.handleSuccess(successResponse);
        const response = successResponse.json();
        if (routeToViewCoupon) {
            this.router.navigate(['/admin-dashboard/coupons/view', 11]);
        } else {
            this.response.message = response.id + ' saved successfully';
        }

    }
}
