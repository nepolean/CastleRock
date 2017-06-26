import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Tax } from './tax';

@Component({
  moduleId: module.id,
  selector: 'tax-action-buttons',
  templateUrl: 'tax-action-buttons-directive.html'
})
export class TaxActionButtonsDirective {

    @Input() tax: Tax;
    @Input() listPage: boolean = false;
    @Output() editTax: EventEmitter<Tax> = new EventEmitter();

    constructor() {}

    private edit(): void {
      this.editTax.emit(this.tax);
    }

}