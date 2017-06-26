import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Coupon } from './coupon';
import { CouponService } from './mock-coupon.service';

@Component({
  moduleId: module.id,
  selector: 'coupon-action-modals',
  templateUrl: 'coupon-action-modals-directive.html'
})
export class CouponActionModalsDirective {

    constructor(private couponService: CouponService) {}
    @Input() coupon: Coupon;
    @Output() deleteCoupon: EventEmitter<Coupon> = new EventEmitter();

    private delete(): void {
      this.deleteCoupon.emit(this.coupon);
    }

}