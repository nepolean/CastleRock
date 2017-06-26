import { EventEmitter } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Profile } from '../models/profile';

@Injectable()
export class ProfileService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private profileUrl = '/api/profile';
  private passwordChangeUrl = '/api/profile/changePassword';
  public profileUpdate: EventEmitter<Profile> = new EventEmitter<Profile>();
  constructor(private http: Http) { }

  getProfile(): Observable<Response> {
    return this.http.get(this.profileUrl);
  }

  updateProfile(profile: Profile): Observable<Response> {
    return this.http
      .post(this.profileUrl, JSON.stringify(profile), {headers: this.headers});
  }

  updatePassword(map: any): Observable<Response> {
    return this.http
      .post(this.passwordChangeUrl, JSON.stringify(map), {headers: this.headers});
  }

  broadcastProfileUpdate(profile: Profile): void {
    this.profileUpdate.emit(profile);
  }
}