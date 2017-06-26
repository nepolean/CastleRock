import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Package } from './package';
import { PackageService } from './mock-package.service';

@Component({
  moduleId: module.id,
  selector: 'package-action-modals',
  templateUrl: 'package-action-modals-directive.html'
})
export class PackageActionModalsDirective {

    constructor(private packageService: PackageService) {}
    @Input() amcPackage: Package;
    @Output() deletePackage: EventEmitter<Package> = new EventEmitter();

    private delete(): void {
      this.deletePackage.emit(this.amcPackage);
    }

}