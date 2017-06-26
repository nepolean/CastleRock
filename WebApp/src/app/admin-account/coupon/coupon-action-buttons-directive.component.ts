import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Coupon } from './coupon';

@Component({
  moduleId: module.id,
  selector: 'coupon-action-buttons',
  templateUrl: 'coupon-action-buttons-directive.html'
})
export class CouponActionButtonsDirective {

    @Input() coupon: Coupon;
    @Input() listPage: boolean = false;
    @Output() editCoupon: EventEmitter<Coupon> = new EventEmitter();

    constructor() {}

    private edit(): void {
      this.editCoupon.emit(this.coupon);
    }

}