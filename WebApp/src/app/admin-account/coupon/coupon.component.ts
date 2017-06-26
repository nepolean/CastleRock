import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { CouponAbstractFormComponent } from './coupon-abstract-form.component';
import { Coupon } from './coupon';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { CouponPaginatorComponent } from './coupon-paginator.component';
import { CouponService } from './mock-coupon.service';
import { CouponFieldsDirective } from './coupon-fields-directive.component';
import 'rxjs/add/operator/switchMap';

@Component({
  moduleId: module.id,
  selector: 'coupon-app',
  templateUrl: 'coupon.component.html'
})
export class CouponComponent extends CouponAbstractFormComponent implements OnInit {

    private coupons: Coupon[];
    private selectedCoupon: Coupon = new Coupon();
    private paginator: Paginator = new Paginator();

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        protected couponService: CouponService,
        protected FormValidationMessageService: FormValidationMessageService) {
            super(router, route, couponService, FormValidationMessageService);
    }

    ngOnInit(): void {
        this.getCoupons();
    }

    private handlePageSelection(paginator: Paginator): void {
        this.getCoupons();
    }

    private getCoupons(): void {
        this.couponService.getCoupons({
            page: this.paginator.currentPage
        }).subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    private searchCoupon(name: string): void {
        this.couponService.getCouponsByName(name)
        .subscribe(
            response => this.handleSuccess(response),
            error => this.handleError(error)
        );
    }

    public handleSuccess(successResponse: any): void {
        const response: any = successResponse.json();
        this.coupons = response.content as Coupon[];
        this.paginator.totalPages = response.totalPages;
        this.paginator.totalElements = response.totalElements;
        this.paginator.last = response.last;
        this.paginator.first = response.first;
        this.paginator.size = response.size;
        this.paginator.currentPage = response.number;
        this.paginator.numberOfElements = response.numberOfElements;
        this.paginator.entity = 'coupons';
    }

    protected handleDeletedSuccess(successResponse: any): void {
        super.handleSuccess(successResponse);
        this.response.message = 'Coupon deleted successfully';
        this.coupons.splice(this.coupons.indexOf(this.selectedCoupon), 1);
    }

    private selectCoupon(coupon: Coupon): void {
        this.selectedCoupon = coupon;
    }
}
