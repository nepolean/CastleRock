import { Injectable } from '@angular/core';
import { Headers, Http, Response, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { User } from './user';

@Injectable()
export class UserService {

  private headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });
  private usersUrl = '/api/admin/users';
  private rolesUrl = '/api/admin/roles';
  constructor(private http: Http) { }

  getUsers(filters?: any): Observable<Response> {
    let params: URLSearchParams = new URLSearchParams();
    if(filters) {
      params.set('page', filters.page);
    }
    return this.http.get(this.usersUrl,{
      search: params
    });
  }

  getUsersByType(filters?: any): Observable<Response> {
    let params: URLSearchParams = new URLSearchParams();
    if(filters) {
      params.set('page', filters.page);
      params.set('type', filters.type);
    }
    return this.http.get(this.usersUrl,{
      search: params
    });
  }

  getUsersByUsername(username: string): Observable<Response> {
    let params: URLSearchParams = new URLSearchParams();
    params.set('username', username);
    return this.http.get(this.usersUrl,{
      search: params
    });
  }

  getAllAvailableRoles(): Observable<Response> {
    return this.http.get(this.rolesUrl);
  }

  getUser(username: string): Observable<Response> {
    const url = `${this.usersUrl}/${username}`;
    return this.http.get(url);
  }

  disableUser(username: string): Observable<Response> {
    const url = `${this.usersUrl}/${username}/disable`;
    return this.http.get(url);
  }

  enableUser(username: string): Observable<Response> {
    const url = `${this.usersUrl}/${username}/enable`;
    return this.http.get(url);
  }

  lockUser(username: string): Observable<Response> {
    const url = `${this.usersUrl}/${username}/lock`;
    return this.http.get(url);
  }

  unlockUser(username: string): Observable<Response> {
    const url = `${this.usersUrl}/${username}/unlock`;
    return this.http.get(url);
  }

  sendPasswordResetLink(username: string): Observable<Response> {
    const url = `${this.usersUrl}/${username}/sendPasswordResetLink`;
    return this.http.get(url);
  }

  deleteUser(username: string): Observable<Response> {
    const url = `${this.usersUrl}/${username}`;
    return this.http.delete(url, {headers: this.headers});
  }

  setRoles(username: string, roles: string[]): Observable<Response> {
    const url = `${this.usersUrl}/${username}/setRoles`;
    return this.http
      .put(url, JSON.stringify(roles), {headers: this.headers});
  }

  createUser(user: User): Observable<Response> {
    return this.http
      .post(this.usersUrl, JSON.stringify(user), {headers: this.headers});
  }

  updateUser(user: User): Observable<Response> {
    const url = `${this.usersUrl}/${user.username}`;
    return this.http
      .put(url, JSON.stringify(user), {headers: this.headers});
  }
}