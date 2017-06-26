import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { CouponAbstractFormComponent } from './coupon-abstract-form.component';
import { CouponActionButtonsDirective } from './coupon-action-buttons-directive.component';
import { CouponActionModalsDirective } from './coupon-action-modals-directive.component';
import { Coupon } from './coupon';
import { CouponService } from './mock-coupon.service';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'coupon-detail',
  templateUrl: 'coupon-detail.component.html'
})
export class CouponDetailComponent extends CouponAbstractFormComponent implements OnInit {

    private coupon: Coupon = new Coupon();

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
}
