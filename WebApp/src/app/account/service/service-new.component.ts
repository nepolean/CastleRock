import { Component,OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router,ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
@Component({
  moduleId: module.id,
  selector: 'app-account-service-new',
  templateUrl: 'service-new.component.html'
})
export class ServiceNewComponent implements OnInit{
    private sub: Subscription;
    private proertyName:any;
    private readonly numberOfSteps: number = 4;
    private currentStep: number = 1;
    private selectedJob:string='';
    private selectedPackage:string='';
    constructor(
        private router: Router,
        private _route: ActivatedRoute,
        private titleService: Title) {
        this.titleService.setTitle('Company | Request new service');
    }
    ngOnInit(): void {
        // this.sub = this._route.params.subscribe(
        //     params => {
        //         let name = params['name'];
        //         this.proertyName=name;
        //         if(this.proertyName==undefined)
        //         {this.proertyName="Purva Supreme"}
        // });
        this.proertyName = localStorage.getItem('appType');
        
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
            return 'buttonDisabled btn btn-primary';
        }
        return 'buttonNext btn btn-primary';
    }

    private getPreviousButtonClass(): string {
        if (this.currentStep === 1) {
            return 'buttonDisabled btn btn-primary';
        }
        return 'buttonPrevious btn btn-primary';
    }

    private getFinishButtonClass(): string {
        if (this.currentStep === this.numberOfSteps) {
            return 'btn btn-default';
        }
        return 'buttonDisabled btn btn-default';
    }

    private submitForm(): void {
        this.router.navigateByUrl('/dashboard/payment');
        console.log('Submitted');
    }

    private selectJob(name:string)
    {
        this.selectedJob=name;
    }
    private selectPackage(name:string)
    {
        this.selectedPackage=name;
    }
}