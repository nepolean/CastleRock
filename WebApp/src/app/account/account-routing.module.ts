import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountComponent } from './account.component';
import { ServiceComponent } from './service/service.component';
import { ServiceNewComponent } from './service/service-new.component';
import { ServiceUpcomingComponent } from './service/service-upcoming.component';
import { ServiceCompletedComponent } from './service/service-completed.component';
import { ServiceCompletedDetailComponent } from './service/service-completed-detail.component';
import { PropertyComponent } from './property/property.component';
import { PropertyDetailComponent } from './property/property-detail.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { SubscriptionNewComponent } from './subscription/subscription-new.component';
import { SubscriptionDetailComponent } from './subscription/subscription-detail.component';
import { SubscriptionRenewalComponent } from './subscription/subscription-renewal.component';
import { UserProfileEditComponent } from './profile/user-profile-edit.component';
import { UserProfileChangePasswordComponent } from './profile/user-profile-change-password.component';
import { MessageComponent } from './message/message.component';
import { TicketBeginAbstractComponent } from './ticket/ticket-begin-abstract.component';
import { TicketComponent } from './ticket/ticket.component';
import { AppAuthGuard } from '../app-auth-guard.service';

const routes: Routes = [
  {
    path: '',
    component: AccountComponent,
    canActivate: [ AppAuthGuard ],
    children: [
      {
        path: '',
        canActivateChild: [ AppAuthGuard ],
        children: [
          {
            path: '',
            redirectTo: '/dashboard/service',
            pathMatch: 'full'
          },
          {
            path: 'service',
            component: ServiceComponent
          },
          {
            path: 'service-new',
            component: ServiceNewComponent
          },
          {
            path: 'service-upcoming',
            component: ServiceUpcomingComponent
          },
          {
            path: 'service-completed',
            component: ServiceCompletedComponent
          },
          {
            path: 'service-completed-detail/:id',
            component: ServiceCompletedDetailComponent
          },
          {
            path: 'property',
            component: PropertyComponent
          },
          {
            path: 'property-detail/:id',
            component: PropertyDetailComponent
          },
          {
            path: 'subscription',
            component: SubscriptionComponent
          },
          {
            path: 'subscription-detail/:id',
            component: SubscriptionDetailComponent
          },
          {
            path: 'subscription-renewal/:id',
            component: SubscriptionRenewalComponent
          },
          {
            path: 'subscription-new',
            component: SubscriptionNewComponent
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
            path: 'message',
            component: MessageComponent
          },
          {
            path: 'ticket',
            component: TicketComponent
          }
        ]
      }
    ]
  }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class AccountRoutingModule {}