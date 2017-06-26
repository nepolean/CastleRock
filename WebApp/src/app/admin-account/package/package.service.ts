import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Package } from './package';

@Injectable()
export class RealPackageService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private packagesUrl = '/api/admin/packages';
  constructor(private http: Http) { }

  getPackages(filters?: any): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    if (filters) {
      params.set('page', filters.page);
    }
    return this.http.get(this.packagesUrl, {
      search: params
    });
  }

  getPackagesByType(type: string): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    params.set('type', type);
    return this.http.get(this.packagesUrl, {
      search: params
    });
  }

  getPackage(id: string): Observable<Response> {
    const url = `${this.packagesUrl}/${id}`;
    return this.http.get(url);
  }

  deletePackage(id: string): Observable<Response> {
    const url = `${this.packagesUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers});
  }

  createPackage(amcPackage: Package): Observable<Response> {
    return this.http
      .post(this.packagesUrl, JSON.stringify(amcPackage), {headers: this.headers});
  }

  updatePackage(amcPackage: Package): Observable<Response> {
    const url = `${this.packagesUrl}/${amcPackage.id}`;
    return this.http
      .put(url, JSON.stringify(amcPackage), {headers: this.headers});
  }
}
