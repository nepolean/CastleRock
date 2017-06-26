import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-account-service-new',
  templateUrl: 'service-new.component.html'
})
export class ServiceNewComponent {

    private readonly numberOfSteps: number = 4;
    private currentStep: number = 1;

    constructor(
        private router: Router,
        private titleService: Title) {
        this.titleService.setTitle('Company | Request new service');
    }

    private goToNext(): void {
         if(this.currentStep === this.numberOfSteps) {
            return;
         }
         this.currentStep++;
    }

    private goToPrevious(): void {
         if(this.currentStep === 1) {
            return;
         }
         this.currentStep--;
    }

    private getWizardStepCircleClass(step: number): string {
        if (this.currentStep === step) {
            return 'selected active';
        }
        if (this.currentStep > step) {
            return 'done';
        }
        return 'disabled';
    }

    private getNextButtonClass(): string {
        if (this.currentStep === this.numberOfSteps) {
            return 'buttonDisabled btn btn-danger';
        }
        return 'buttonNext btn btn-danger';
    }

    private getPreviousButtonClass(): string {
        if (this.currentStep === 1) {
            return 'buttonDisabled btn btn-danger';
        }
        return 'buttonPrevious btn btn-danger';
    }

    private getFinishButtonClass(): string {
        if (this.currentStep === this.numberOfSteps) {
            return 'btn btn-default';
        }
        return 'buttonDisabled btn btn-default';
    }

    private submitForm(): void {
        console.log('Submitted');
    }
}