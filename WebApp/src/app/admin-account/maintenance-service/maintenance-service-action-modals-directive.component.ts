import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MaintenanceService } from './maintenance-service';
import { MaintenanceServiceService } from './mock-maintenance-service.service';

@Component({
  moduleId: module.id,
  selector: 'maintenance-service-action-modals',
  templateUrl: 'maintenance-service-action-modals-directive.html'
})
export class MaintenanceServiceActionModalsDirective {

    @Input() maintenanceService: MaintenanceService;
    @Output() deleteMaintenanceService: EventEmitter<MaintenanceService> = new EventEmitter();

    constructor(private maintenanceServiceService: MaintenanceServiceService) {}

    private delete(): void {
      this.deleteMaintenanceService.emit(this.maintenanceService);
    }

}