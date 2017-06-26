import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Package } from './package';
import { PACKAGES } from './mock-packages';

@Injectable()
export class PackageService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private packagesUrl = '/api/admin/packages';
  constructor(private http: Http) { }

  getPackages(filters?: any): Observable<Response> {
    const pagedPackages = {
      'content': PACKAGES,
      'totalElements': PACKAGES.length,
      'last': true,
      'totalPages': 1,
      'size': PACKAGES.length,
      'number': 0,
      'sort': null,
      'first': true,
      'numberOfElements': PACKAGES.length
    };
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(pagedPackages)
    }));
    return Observable.of(response);
  }

  getPackagesByType(type: string): Observable<Response> {
    const filteredArray: Package[] = [PACKAGES.find(element => element.type === type)];
    const pagedPackages = {
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
      body: JSON.stringify(pagedPackages)
    }));
    return Observable.of(response);
  }

  getPackage(id: string): Observable<Response> {
    const amcPackage: Package = PACKAGES.find(element => element.id === id);
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(amcPackage)
    }));
    return Observable.of(response);
  }

  deletePackage(id: string): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: "Deleted successfully"
    }));
    return Observable.of(response);
  }

  createPackage(amcPackage: Package): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(amcPackage)
    }));
    return Observable.of(response);
  }

  updatePackage(amcPackage: Package): Observable<Response> {
    const filteredPackage: Package = PACKAGES.find(element => element.id === amcPackage.id);
    const index = PACKAGES.indexOf(filteredPackage);
    if (index > 0) {
      PACKAGES[index] = amcPackage;
    }
    /* Nothing happens */
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(amcPackage)
    }));
    return Observable.of(response);
  }
}
