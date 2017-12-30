import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { FormValidationMessageService } from '../../common/page-utils/form/form-validation-message.service';
import { MaintenanceService } from './maintenance-service';
import { IMyOptions, IMyDateRangeModel } from 'mydaterangepicker';
import { Category } from 'app/admin-account/maintenance-service/category';
import { Tax } from 'app/admin-account/tax/tax';
import { TaxService } from 'app/admin-account/tax/tax.service';
import { SimpleChange } from '@angular/core/src/change_detection/change_detection_util';

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
      this.maintenanceService.type = 'AssetService';
      this.amenityTypes = this.category.getAmenities(categoryName);
      console.log("amenties ", this.amenityTypes);
    } else {
      this.onAmenitySelection(categoryName, null);
      this.maintenanceService.type = 'GenericService'
    }

  }

  onAmenitySelection(categoryName: string, amenityNames: string[]) {
    console.log("categoryName :", categoryName);
    console.log("amenityNames : ", amenityNames);
    this.skillTypes = this.category.getSkills(categoryName, amenityNames);
  }

  private myDateRangePickerOptions: IMyOptions = {};
  private dateRange: Object = {};

  constructor(
    private FormValidationMessageService: FormValidationMessageService,
    private taxService: TaxService) {

  }

  ngOnInit(): void {
    console.log("In field directive ngOnInit service ", this.maintenanceService);
    this.getTaxes();
    console.log("maintenance service created ", this.editMode);
    console.log("onInit original maintenance object ", this.maintenanceService);
    if (this.editMode) {
    }
  }

  ngOnChanges(changes: SimpleChange): void {
    console.log("I am in fields directive after changes ", this.maintenanceService);
    if (this.maintenanceService.category) {
      this.onCategorySelection(this.maintenanceService.category);
      this.onAmenitySelection(
        this.maintenanceService.category,
        this.maintenanceService.amenities)
    }
  }


  getTaxes() {
    this.taxService.getTaxes(
    ).subscribe(
      response => this.assignTax(response),
      error => console.error('Error fetching tax info ', error)
      );
    //this.maintenanceService.tax = new Tax();
    //TODO need to implement api here
  }

  assignTax(response) {
    this.taxes = response.json().content as Tax[]
    if (!this.editMode)
      this.maintenanceService.tax = this.taxes[0];
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
