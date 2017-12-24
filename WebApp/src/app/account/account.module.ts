import { NgModule } from '@angular/core';
import { CommonModule ,APP_BASE_HREF, Location, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppCommonModule } from '../common/app-common.module';
import { AccountRoutingModule } from './account-routing.module';
import { AccountSideBarComponent } from './page-sections/sidebar/account-sidebar.component';
import { AccountPaginatorComponent } from './page-sections/paginator/account-paginator.component';
import { AccountComponent } from './account.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { ServiceComponent } from './service/service.component';
import { ServiceBeginAbstractComponent } from './service/service-begin-abstract.component';
import { ServiceNewComponent } from './service/service-new.component';
import { ServiceUpcomingComponent } from './service/service-upcoming.component';
import { ServiceCompletedComponent } from './service/service-completed.component';
import { ServiceCompletedDetailComponent } from './service/service-completed-detail.component';
import { CCTVBeginAbstractComponent} from './cctv/cctv-begin-abstract.component';
import { CCTVComponent } from './cctv/cctv.component';
import { CommunityComponent } from './community/community.component';
import { CommunityBeginAbstractComponent } from './community/community-begin-abstract.component';
import { CommunityMembersComponent } from './community/community-members.component';
import { CommunityLinkComponent } from './community/communitylink.component';
import { PaymentComponent } from './payment/payment.component';
import { PaymentPendingComponent } from './payment/payment-pending.component';
import { PaymentBeginAbstractComponent } from './payment/payment-begin-abstract.component';
import { PaymentHistoryComponent } from './payment/payment-history.component';
import { PaymentDetailComponent } from './payment/payment-detail.component';
import { PaymentSuccessComponent } from './payment/payment-success.component';
import { PropertyBeginAbstractComponent } from './property/property-begin-abstract.component';
import { PropertyComponent } from './property/property.component';
import { PropertyDetailComponent } from './property/property-detail.component';
import { SubscriptionBeginAbstractComponent } from './subscription/subscription-begin-abstract.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { SubscriptionNewComponent } from './subscription/subscription-new.component';
import { SubscriptionDetailComponent } from './subscription/subscription-detail.component';
import { SubscriptionRenewalComponent } from './subscription/subscription-renewal.component';
import { UserProfileBeginAbstractComponent } from './profile/user-profile-begin-abstract.component';
import { UserProfileEditComponent } from './profile/user-profile-edit.component';
import { UserProfileChangePasswordComponent } from './profile/user-profile-change-password.component';
import { UtilityBillBeginAbstractComponent } from './utilitybill/utility-bill-begin-abstract.component';
import { UtilityBillComponent } from './utilitybill/utility-bill.component';
import { MessageBeginAbstractComponent } from './message/message-begin-abstract.component';
import { MessageComponent } from './message/message.component';
import { TicketBeginAbstractComponent } from './ticket/ticket-begin-abstract.component';
import { TicketComponent } from './ticket/ticket.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    AccountRoutingModule
  ],
  declarations: [
    AccountPaginatorComponent,
    AccountComponent,
    CCTVBeginAbstractComponent,
    CCTVComponent,
    DashboardComponent,
    HomeComponent,
    ServiceComponent,
    ServiceBeginAbstractComponent,
    ServiceNewComponent,
    ServiceUpcomingComponent,
    ServiceCompletedComponent,
    ServiceCompletedDetailComponent,
    CommunityComponent,
    CommunityBeginAbstractComponent,
    CommunityMembersComponent,
    CommunityLinkComponent,
    PaymentComponent,
    PaymentBeginAbstractComponent,
    PaymentPendingComponent,
    PaymentHistoryComponent,
    PaymentDetailComponent,
    PaymentSuccessComponent,
    PropertyBeginAbstractComponent,
    PropertyComponent,
    PropertyDetailComponent,
    SubscriptionBeginAbstractComponent,
    SubscriptionComponent,
    SubscriptionNewComponent,
    SubscriptionDetailComponent,
    SubscriptionRenewalComponent,
    UserProfileBeginAbstractComponent,
    UserProfileEditComponent,
    UserProfileChangePasswordComponent,
    UtilityBillBeginAbstractComponent,
    UtilityBillComponent,
    MessageBeginAbstractComponent,
    MessageComponent,
    TicketComponent,
    TicketBeginAbstractComponent,
    AccountSideBarComponent
  ],
  providers: [
    { provide: LocationStrategy, useClass: PathLocationStrategy }, 
    { provide: APP_BASE_HREF, useValue: '/account' }
  ]
})
export class AccountModule { }