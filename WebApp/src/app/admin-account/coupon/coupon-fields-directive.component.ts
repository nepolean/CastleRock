import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { Coupon } from './coupon';
import { IMyOptions, IMyDateRangeModel} from 'mydaterangepicker';

@Component({
  moduleId: module.id,
  selector: 'coupon-fields',
  templateUrl: 'coupon-fields-directive.html'
})
export class CouponFieldsDirective implements OnInit {

    @Input() coupon: Coupon;
    @Input() editMode: boolean;

    private myDateRangePickerOptions: IMyOptions = {};
    private dateRange: Object = {};

    constructor(private FormValidationMessageService: FormValidationMessageService) {

    }

    ngOnInit(): void {
        this.setDateRangeOptions();
        this.setDateRange();
    }

    private setDateRangeOptions(): void {
        const date = new Date();
        this.myDateRangePickerOptions.dateFormat = 'dd mmm yyyy';
        this.myDateRangePickerOptions.inputValueRequired = true;
        this.myDateRangePickerOptions.disableUntil = {
                year: date.getFullYear(),
                month: date.getMonth() + 1,
                day: date.getDate()
        };
        this.myDateRangePickerOptions.editableDateRangeField = false;
        this.myDateRangePickerOptions.indicateInvalidDateRange = true;
        this.myDateRangePickerOptions.inputValueRequired = true;
        this.myDateRangePickerOptions.width = '100%';
    }

    private setDateRange(): void {
        // Set date range (today) using the setValue function
        const beginDate: Date = this.coupon.validFrom ? new Date(this.coupon.validFrom) : new Date();
        const endDate: Date = this.coupon.validTo ? new Date(this.coupon.validTo) : new Date();
        this.dateRange = {
            beginDate: {
                year: beginDate.getFullYear(),
                month: beginDate.getMonth() + 1,
                day: beginDate.getDate()
            },
            endDate: {
                year: endDate.getFullYear(),
                month: endDate.getMonth() + 1,
                day: endDate.getDate()
            }
        };
        this.coupon.validFrom = beginDate.toISOString().slice(0, 10);
        this.coupon.validTo = endDate.toISOString().slice(0, 10);
    }

    private onDateRangeChanged(event: IMyDateRangeModel) {
        // event properties are: event.date, event.jsdate, event.formatted and event.epoc
        this.coupon.validFrom = event.beginJsDate ? event.beginJsDate.toISOString().slice(0, 10) : '';
        this.coupon.validTo = event.endJsDate ? event.endJsDate.toISOString().slice(0, 10) : '';
    }
}
