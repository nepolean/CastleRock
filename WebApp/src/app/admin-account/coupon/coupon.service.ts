import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Coupon } from './coupon';

@Injectable()
export class RealCouponService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private couponsUrl = '/api/admin/coupons';
  constructor(private http: Http) { }

  getCoupons(filters?: any): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    if (filters) {
      params.set('page', filters.page);
    }
    return this.http.get(this.couponsUrl, {
      search: params
    });
  }

  getCouponsByName(name: string): Observable<Response> {
    const params: URLSearchParams = new URLSearchParams();
    params.set('name', name);
    return this.http.get(this.couponsUrl, {
      search: params
    });
  }

  getCoupon(id: string): Observable<Response> {
    const url = `${this.couponsUrl}/${id}`;
    return this.http.get(url);
  }

  deleteCoupon(id: string): Observable<Response> {
    const url = `${this.couponsUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers});
  }

  createCoupon(coupon: Coupon): Observable<Response> {
    return this.http
      .post(this.couponsUrl, JSON.stringify(coupon), {headers: this.headers});
  }

  updateCoupon(coupon: Coupon): Observable<Response> {
    const url = `${this.couponsUrl}/${coupon.id}`;
    return this.http
      .put(url, JSON.stringify(coupon), {headers: this.headers});
  }
}
