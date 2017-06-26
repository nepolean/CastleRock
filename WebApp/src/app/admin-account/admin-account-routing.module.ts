import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminAccountComponent } from './admin-account.component';
import { UserProfileEditComponent } from './profile/user-profile-edit.component';
import { UserProfileChangePasswordComponent } from './profile/user-profile-change-password.component';
import { HomeComponent } from './home/home.component';
import { MessageComponent } from './home/message/message.component';
import { AppAuthGuard } from '../app-auth-guard.service';

const routes: Routes = [
  {
    path: '',
    component: AdminAccountComponent,
    canActivate: [ AppAuthGuard ],
    children: [
      {
        path: '',
        redirectTo: '/admin-dashboard/home/messages',
        pathMatch: 'full'
      },
      {
        path: 'home',
        component: HomeComponent,
        children: [
          {
            path: '',
            redirectTo: '/admin-dashboard/home/messages',
            pathMatch: 'full'
          },
          {
            path: 'messages',
            component: MessageComponent
          }
        ]
      },
      {
        path: 'profile-edit',
        component: UserProfileEditComponent
      },
      {
        path: 'profile-change-password',
        component: UserProfileChangePasswordComponent
      },
      {
        path: 'users',
        loadChildren: 'app/admin-account/user/user.module#UserModule'
      },
      {
        path: 'taxes',
        loadChildren: 'app/admin-account/tax/tax.module#TaxModule'
      },
      {
        path: 'coupons',
        loadChildren: 'app/admin-account/coupon/coupon.module#CouponModule'
      },
      {
        path: 'services',
        loadChildren: 'app/admin-account/maintenance-service/maintenance-service.module#MaintenanceServiceModule'
      },
      {
        path: 'packages',
        loadChildren: 'app/admin-account/package/package.module#PackageModule'
      }
    ]
  }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class AdminAccountRoutingModule {}
