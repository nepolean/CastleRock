import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Tax } from './tax';
import { TaxService } from './mock-tax.service';

@Component({
  moduleId: module.id,
  selector: 'tax-action-modals',
  templateUrl: 'tax-action-modals-directive.html'
})
export class TaxActionModalsDirective {

    constructor(private taxService: TaxService) {}
    @Input() tax: Tax;
    @Output() deleteTax: EventEmitter<Tax> = new EventEmitter();

    private delete(): void {
      this.deleteTax.emit(this.tax);
    }

}