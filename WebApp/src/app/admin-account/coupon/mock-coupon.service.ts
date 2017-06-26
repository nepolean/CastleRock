import { Injectable } from '@angular/core';
import { Headers, Http, Response, ResponseOptions, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Coupon } from './coupon';
import { COUPONS } from './mock-coupons';

@Injectable()
export class CouponService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private couponsUrl = '/api/admin/coupons';
  constructor(private http: Http) { }

  getCoupons(filters?: any): Observable<Response> {
    const pagedCoupons = {
      'content': COUPONS,
      'totalElements': COUPONS.length,
      'last': true,
      'totalPages': 1,
      'size': COUPONS.length,
      'number': 0,
      'sort': null,
      'first': true,
      'numberOfElements': COUPONS.length
    };
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(pagedCoupons)
    }));
    return Observable.of(response);
  }

  getCouponsByName(name: string): Observable<Response> {
    const filteredArray: Coupon[] = [COUPONS.find(element => element.name === name)];
    const pagedCoupons = {
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
      body: JSON.stringify(pagedCoupons)
    }));
    return Observable.of(response);
  }

  getCoupon(id: string): Observable<Response> {
    const coupon: Coupon = COUPONS.find(element => element.id === id);
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(coupon)
    }));
    return Observable.of(response);
  }

  deleteCoupon(id: string): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: "Deleted successfully"
    }));
    return Observable.of(response);
  }

  createCoupon(coupon: Coupon): Observable<Response> {
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(coupon)
    }));
    return Observable.of(response);
  }

  updateCoupon(coupon: Coupon): Observable<Response> {
    const filteredCoupon: Coupon = COUPONS.find(element => element.id === coupon.id);
    const index = COUPONS.indexOf(filteredCoupon);
    if (index > 0) {
      COUPONS[index] = coupon;
    }
    /* Nothing happens */
    const response = new Response( new ResponseOptions({
      body: JSON.stringify(coupon)
    }));
    return Observable.of(response);
  }
}
