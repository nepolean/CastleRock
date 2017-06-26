import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MaintenanceService } from './maintenance-service';

@Component({
  moduleId: module.id,
  selector: 'maintenance-service-action-buttons',
  templateUrl: 'maintenance-service-action-buttons-directive.html'
})
export class MaintenanceServiceActionButtonsDirective {

    @Input() maintenanceService: MaintenanceService;
    @Input() listPage: boolean = false;
    @Output() editMaintenanceService: EventEmitter<MaintenanceService> = new EventEmitter();

    constructor() {}

    private edit(): void {
      this.editMaintenanceService.emit(this.maintenanceService);
    }

}