import { Component } from '@angular/core';

@Component({
  moduleId: module.id,
  template: ''
})
export class AbstractFormComponent {

    protected response: any = {
        flags : {
            available: false,
            error: false,
            loading: false
        },
        message : "",
        fieldErrors : []
    }

    constructor() {}

    protected setLoadingState(): void {
        this.response.flags.available = true;
        this.response.flags.loading = true;
        this.response.fieldErrors = [];
        this.response.message = "Loading...";
    }

    protected resetLoadingState(): void {
        this.response.flags.available = true;
        this.response.flags.loading = false;
    }

    protected handleSuccess(successResponse: any, userSuppliedObject?: any): void {
        this.resetLoadingState();
        this.response.flags.error = false;
    }

    protected handleError(errorResponse: any): void {
        this.resetLoadingState();
        this.response.flags.error = true;
        if(errorResponse.status == 504) {
            this.response.message = "Request timed out.";
            return;
        }
        let error = errorResponse.json();
        if(!error) {
            this.response.message = "Oops! Something went wrong in getting the response.";
            return;
        }
        let action = error.action ? error.action : "";
        if(error.message) {
            if(error.message == "No message available") {
                this.response.message = error.status + " : " + error.message;
                return; 
            }
            if(error.message == "VALIDATION FAILED") {
                for(let key in error.errors)
                    this.response.fieldErrors.push(error.errors[key]);
            }
            this.response.message = error.action ? error.message + " : " + action : error.message;
        } else {
            this.response.message = "Oops! Something went wrong in the server."; 
        }
    }
}