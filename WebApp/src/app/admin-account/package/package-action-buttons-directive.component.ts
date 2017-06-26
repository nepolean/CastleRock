import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Package } from './package';

@Component({
  moduleId: module.id,
  selector: 'package-action-buttons',
  templateUrl: 'package-action-buttons-directive.html'
})
export class PackageActionButtonsDirective {

    @Input() amcPackage: Package;
    @Input() listPage: boolean = false;
    @Output() editPackage: EventEmitter<Package> = new EventEmitter();

    constructor() {}

    private edit(): void {
      this.editPackage.emit(this.amcPackage);
    }

}