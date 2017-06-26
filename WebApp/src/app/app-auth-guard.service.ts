import { Injectable } from '@angular/core';
import {
  CanActivate, Router, Route,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { AppAuthService } from './app-auth.service';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class AppAuthGuard implements CanActivate {

  constructor(private authService: AppAuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    let url: string = state.url;
    if(url.indexOf('admin-dashboard') != -1) {
      return this.authenticateAdmin();
    }
    return this.authenticateUser();
  }

  canLoad(route: Route): Observable<boolean> {
    if(route.path.indexOf('admin-dashboard') != -1) {
      return this.authenticateAdmin();
    }
    return this.authenticateUser();
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.canActivate(route, state);
  }

  private authenticateUser(): Observable<boolean> {
    return this.authService.authenticateUser()
      .map(e => {
            if (e) {
                return true;
            }
        }).catch(() => {
            window.location.href = "http://localhost/login";
            return Observable.of(false);
      });
  }

  private authenticateAdmin(): Observable<boolean> {
    return this.authService.authenticateAdmin()
      .map(e => {
            if (e) {
                return true;
            }
        }).catch(() => {
            window.location.href = "http://localhost/login";
            return Observable.of(false);
      });
  }
}
