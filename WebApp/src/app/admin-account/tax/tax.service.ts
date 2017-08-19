import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Tax } from './tax';

@Injectable()
export class TaxService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private taxesUrl = '/api/v1/admin/tax';
  constructor(private http: Http) { }

  getTaxes(filters?: any): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    if (filters) {
      params.set('page', filters.page);
    }
    return this.http.get(this.taxesUrl, {
      search: params
    });
  }

  getTaxesByType(type: string): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    params.set('type', type);
    return this.http.get(this.taxesUrl, {
      search: params
    });
  }

  getTax(id: string): Observable<Response> {
    const url = `${this.taxesUrl}/${id}`;
    return this.http.get(url);
  }

  deleteTax(id: string): Observable<Response> {
    const url = `${this.taxesUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers});
  }

  createTax(tax: Tax): Observable<Response> {
    return this.http
      .post(this.taxesUrl, JSON.stringify(tax), {headers: this.headers});
  }

  updateTax(tax: Tax): Observable<Response> {
    const url = `${this.taxesUrl}/${tax.id}`;
    return this.http
      .put(url, JSON.stringify(tax), {headers: this.headers});
  }
}
