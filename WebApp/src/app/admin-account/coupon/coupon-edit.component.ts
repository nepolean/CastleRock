import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { CouponAbstractFormComponent } from './coupon-abstract-form.component';
import { Coupon } from './coupon';
import { CouponService } from './mock-coupon.service';
import { CouponFieldsDirective } from './coupon-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'coupon-edit',
  templateUrl: 'coupon-edit.component.html'
})
export class CouponEditComponent extends CouponAbstractFormComponent implements OnInit {

    private coupon: Coupon = new Coupon();
    private editMode: boolean = true;

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected couponService: CouponService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, couponService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.couponService.getCoupon(params['id']))
            .subscribe(
                response => this.handleSuccess(response),
                error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        this.coupon = successResponse.json() as Coupon;
    }

    private updateCoupon(): void {
        this.setLoadingState();
        this.couponService.updateCoupon(this.coupon)
        .subscribe(
            response => this.handleUpdatedSuccess(response),
            error => this.handleError(error)
        );
    }

    private onSubmit(): void {
        this.updateCoupon();
    }

    public handleUpdatedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.router.navigate(['/admin-dashboard/coupons/view', this.coupon.id]);
    }
}
