import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './not-found.component';
import { AppAuthGuard } from './app-auth-guard.service';

const routes: Routes = [
  {
    path: 'dashboard',
    //canLoad: [ AppAuthGuard ],
    loadChildren: 'app/account/account.module#AccountModule'
  },
  {
    path: 'admin-dashboard',
    loadChildren: 'app/admin-account/admin-account.module#AdminAccountModule'
  },
  {
    path: 'agent-dashboard',
    loadChildren: 'app/agent-account/agent-account.module#AgentAccountModule'
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];
@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}