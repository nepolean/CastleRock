import { Injectable } from '@angular/core';
import { Headers, Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';

@Injectable()
export class AppAuthService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private userAuthenticationUrl = '/api/profile/isUser';
  private adminAuthenticatinUrl = '/api/profile/isAdmin';
  constructor(private http: Http) { }

  authenticate(): Observable<Response> {
    return this.http.get(this.userAuthenticationUrl, { headers: this.headers });
  }

  authenticateUser(): Observable<Response> {
    return this.http.get(this.userAuthenticationUrl, { headers: this.headers });
  }

  authenticateAdmin(): Observable<Response> {
    return this.http.get(this.adminAuthenticatinUrl, { headers: this.headers });
  }
}