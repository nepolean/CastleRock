import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceService } from './maintenance-service';
import { IMyOptions, IMyDateRangeModel } from 'mydaterangepicker';
import { Category } from 'app/admin-account/maintenance-service/category';
import { Tax } from '../../common/objects/tax'
@Component({
    moduleId: module.id,
    selector: 'maintenance-service-fields',
    templateUrl: 'maintenance-service-fields-directive.html'
})

//export class MaintenanceServiceFieldsDirective implements OnInit {
export class MaintenanceServiceFieldsDirective {

    @Input() maintenanceService: MaintenanceService;
    @Input() editMode: boolean;

    category: Category = new Category();
    categoryNames: string[] = this.category.getCategories();
    amenityTypes: string[] = [];
    skillTypes: string[] = [];
    taxes: Tax[] = [];


    private onCategorySelection(categoryName): void {
        console.log("category selected", categoryName);
        if (categoryName == "ASSET") {
            this.amenityTypes = this.category.getAmenities(categoryName);
            console.log("amenties ", this.amenityTypes);
        } else
            this.onAmenitySelection(categoryName, null);
    }

    onAmenitySelection(categoryName: string, amenityNames: string[]) {
        console.log("categoryName :", categoryName);
        console.log("amenityNames : ", amenityNames);
        this.skillTypes = this.category.getSkills(categoryName, amenityNames);
    }

    private myDateRangePickerOptions: IMyOptions = {};
    private dateRange: Object = {};

    constructor(private FormValidationMessageService: FormValidationMessageService) {
    }

    ngOnInit(): void {
        this.getTaxes();
    }
    getTaxes() {
        //TODO need to implement api here
    }
    
    /* ngOnInit(): void {
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
        const beginDate: Date = this.maintenanceService.validFrom ? new Date(this.maintenanceService.validFrom) : new Date();
        const endDate: Date = this.maintenanceService.validTo ? new Date(this.maintenanceService.validTo) : new Date();
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
        this.maintenanceService.validFrom = beginDate.toISOString().slice(0, 10);
        this.maintenanceService.validTo = endDate.toISOString().slice(0, 10);
    }

    private onDateRangeChanged(event: IMyDateRangeModel) {
        // event properties are: event.date, event.jsdate, event.formatted and event.epoc
        this.maintenanceService.validFrom = event.beginJsDate ? event.beginJsDate.toISOString().slice(0, 10) : '';
        this.maintenanceService.validTo = event.endJsDate ? event.endJsDate.toISOString().slice(0, 10) : '';
    } */
}
