import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { MaintenanceService } from './maintenance-service';
import { MAINTENANCESERVICES } from './mock-maintenance-services';

@Injectable()
export class MaintenanceServiceService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private servicesUrl = '/api/admin/services';
  constructor(private http: Http) { }

  getMaintenanceServices(filters?: any): Observable<Response> {
    const pagedMaintenanceServices = {
      'content': MAINTENANCESERVICES,
      'totalElements': MAINTENANCESERVICES.length,
      'last': true,
      'totalPages': 1,
      'size': MAINTENANCESERVICES.length,
      'number': 0,
      'sort': null,
      'first': true,
      'numberOfElements': MAINTENANCESERVICES.length
    };
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(pagedMaintenanceServices)
    }));
    return Observable.of(response);
  }

  getMaintenanceServicesByName(name: string): Observable<Response> {
    const filteredArray: MaintenanceService[] = [MAINTENANCESERVICES.find(element => element.name === name)];
    const pagedMaintenanceServices = {
      'content': filteredArray,
      'totalElements': filteredArray.length,
      'last': true,
      'totalPages': 1,
      'size': filteredArray.length,
      'number': 0,
      'sort': null,
      'first': true,
      'numberOfElements': filteredArray.length
    };
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(pagedMaintenanceServices)
    }));
    return Observable.of(response);
  }

  getMaintenanceService(id: string): Observable<Response> {
    const maintenanceService: MaintenanceService = MAINTENANCESERVICES.find(element => element.id === id);
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(maintenanceService)
    }));
    return Observable.of(response);
  }

  deleteMaintenanceService(id: string): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: "Deleted successfully"
    }));
    return Observable.of(response);
  }

  createMaintenanceService(maintenanceService: MaintenanceService): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(maintenanceService)
    }));
    return Observable.of(response);
  }

  updateMaintenanceService(maintenanceService: MaintenanceService): Observable<Response> {
    const filteredMaintenanceService: MaintenanceService = MAINTENANCESERVICES.find(element => element.id === maintenanceService.id);
    const index = MAINTENANCESERVICES.indexOf(filteredMaintenanceService);
    if (index > 0) {
      MAINTENANCESERVICES[index] = maintenanceService;
    }
    /* Nothing happens */
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(maintenanceService)
    }));
    return Observable.of(response);
  }

  updateOneTimePlan(maintenanceId: string, oneTimePlan: any): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: "Updated"
    }));
    return Observable.of(response);
  }

  updateSubscriptionPlan(maintenanceId: string, subscriptionPlan: any): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: "Updated"
    }));
    return Observable.of(response);
  }
}
