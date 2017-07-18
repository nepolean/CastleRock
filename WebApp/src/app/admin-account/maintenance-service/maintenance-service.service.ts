import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { MaintenanceService } from './maintenance-service';

@Injectable()
export class RealMaintenanceServiceService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private servicesUrl = '/api/admin/services';
  constructor(private http: Http) { }

  getMaintenanceServices(filters?: any): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    if (filters) {
      params.set('page', filters.page);
    }
    return this.http.get(this.servicesUrl, {
      search: params
    });
  }

  getMaintenanceServicesByName(name: string): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    params.set('name', name);
    return this.http.get(this.servicesUrl, {
      search: params
    });
  }

  getMaintenanceService(id: string): Observable<Response> {
    const url = `${this.servicesUrl}/${id}`;
    return this.http.get(url);
  }

  deleteMaintenanceService(id: string): Observable<Response> {
    const url = `${this.servicesUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers});
  }

  createMaintenanceService(maintenanceService: MaintenanceService): Observable<Response> {
    return this.http
      .post(this.servicesUrl, JSON.stringify(maintenanceService), {headers: this.headers});
  }

  updateMaintenanceService(maintenanceService: MaintenanceService): Observable<Response> {
    const url = `${this.servicesUrl}/${maintenanceService.id}`;
    return this.http
      .put(url, JSON.stringify(maintenanceService), {headers: this.headers});
  }

  updateOneTimePlan(maintenanceServiceId: string, oneTimePlan: any): Observable<Response> {
    const url = `${this.servicesUrl}/api/v1/admin/service/${maintenanceServiceId}/asset/onetime`;
    return this.http
      .post(url, JSON.stringify(oneTimePlan), {headers: this.headers});
  }

  updateSubscriptionPlan(maintenanceServiceId: string, subscriptionPlan: any): Observable<Response> {
    const url = `${this.servicesUrl}/api/v1/admin/service/${maintenanceServiceId}/asset/subscription`;
    return this.http
      .post(url, JSON.stringify(subscriptionPlan), {headers: this.headers});
  }
}
