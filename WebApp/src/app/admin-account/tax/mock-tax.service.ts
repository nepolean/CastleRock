import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Tax } from './tax';
import { TAXES } from './mock-taxes';

@Injectable()
export class TaxService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private taxesUrl = '/api/v1/admin/tax';
  constructor(private http: Http) { }

  getTaxes(filters?: any): Observable<Response> {
    const pagedTaxes = {
      'content': TAXES,
      'totalElements': TAXES.length,
      'last': true,
      'totalPages': 1,
      'size': TAXES.length,
      'number': 0,
      'sort': null,
      'first': true,
      'numberOfElements': TAXES.length
    };
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(pagedTaxes)
    }));
    return Observable.of(response);
  }

  getTaxesByType(type: string): Observable<Response> {
    const filteredArray: Tax[] = [TAXES.find(element => element.type === type)];
    const pagedTaxes = {
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
      body: JSON.stringify(pagedTaxes)
    }));
    return Observable.of(response);
  }

  getTax(id: string): Observable<Response> {
    const tax: Tax = TAXES.find(element => element.id === id);
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(tax)
    }));
    return Observable.of(response);
  }

  deleteTax(id: string): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: "Deleted successfully"
    }));
    return Observable.of(response);
  }

  createTax(tax: Tax): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(tax)
    }));
    return Observable.of(response);
  }

  updateTax(tax: Tax): Observable<Response> {
    const filteredTax: Tax = TAXES.find(element => element.id === tax.id);
    const index = TAXES.indexOf(filteredTax);
    if (index > 0) {
      TAXES[index] = tax;
    }
    /* Nothing happens */
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(tax)
    }));
    return Observable.of(response);
  }
}
