import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgentAccountComponent } from './agent-account.component';
import { UserProfileEditComponent } from '../common/profile/user-profile-edit.component';
import { UserProfileChangePasswordComponent } from '../common/profile/user-profile-change-password.component';
import { HomeComponent } from './home/home.component';
import { MessageComponent } from './home/message/message.component';
import { AppAuthGuard } from '../app-auth-guard.service';

const routes: Routes = [
  {
    path: '',
    component: AgentAccountComponent,
    //canActivate: [ AppAuthGuard ],
    children: [
      {
        path: '',
        redirectTo: '/agent-dashboard/home/messages',
        pathMatch: 'full'
      },
      {
        path: 'home',
        component: HomeComponent,
        children: [
          {
            path: '',
            redirectTo: '/agent-dashboard/home/messages',
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
    ]
  }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class AgentAccountRoutingModule {}
