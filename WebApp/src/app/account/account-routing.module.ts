import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountComponent } from './account.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { ServiceComponent } from './service/service.component';
import { ServiceNewComponent } from './service/service-new.component';
import { ServiceUpcomingComponent } from './service/service-upcoming.component';
import { ServiceCompletedComponent } from './service/service-completed.component';
import { ServiceCompletedDetailComponent } from './service/service-completed-detail.component';
import { CommunityComponent } from './community/community.component';
import { CommunityMembersComponent } from './community/community-members.component';
import { PaymentComponent } from './payment/payment.component';
import { PaymentPendingComponent } from './payment/payment-pending.component';
import { PaymentHistoryComponent } from './payment/payment-history.component';
import { PaymentDetailComponent } from './payment/payment-detail.component';
import { PaymentSuccessComponent } from './payment/payment-success.component';
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
        component: DashboardComponent,
        children: [
          {
            path: '',
            redirectTo: '/dashboard/home',
            pathMatch: 'full'
          },
          {
            path: 'home',
            component: HomeComponent,
            children: [
              {
                path: '',
                component: ServiceComponent
              }
            ]
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
            path: 'message',
            component: MessageComponent
          },
          {
            path: 'community',
            component: CommunityComponent
          },
          {
            path: 'community-members',
            component: CommunityMembersComponent
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
        path: 'ticket',
        component: TicketComponent
      },
      {
        path: 'payment',
        component: PaymentComponent
      },
      {
        path: 'payment-pending',
        component: PaymentPendingComponent
      },
      {
        path: 'payment-history',
        component: PaymentHistoryComponent
      },
      {
        path: 'payment-detail/:id',
        component: PaymentDetailComponent
      },
      {
        path: 'payment-success',
        component: PaymentSuccessComponent
      }
    ]
  }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class AccountRoutingModule {}