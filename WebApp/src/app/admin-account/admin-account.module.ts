import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppCommonModule } from '../common/app-common.module';
import { AdminAccountRoutingModule } from './admin-account-routing.module';
import { AdminAccountSideBarComponent } from './page-sections/sidebar/admin-account-sidebar.component';
import { AdminAccountPaginatorComponent } from './page-sections/paginator/admin-account-paginator.component';
import { AdminAccountComponent } from './admin-account.component';
import { UserProfileBeginAbstractComponent } from './profile/user-profile-begin-abstract.component';
import { UserProfileEditComponent } from './profile/user-profile-edit.component';
import { UserProfileChangePasswordComponent } from './profile/user-profile-change-password.component';
import { MessageBeginAbstractComponent } from './home/message/message-begin-abstract.component';
import { HomeComponent } from './home/home.component';
import { MessageComponent } from './home/message/message.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    AdminAccountRoutingModule
  ],
  declarations: [
    AdminAccountPaginatorComponent,
    AdminAccountComponent,
    HomeComponent,
    UserProfileBeginAbstractComponent,
    UserProfileEditComponent,
    UserProfileChangePasswordComponent,
    MessageBeginAbstractComponent,
    MessageComponent,
    AdminAccountSideBarComponent
  ],
  providers: [
  ]
})
export class AdminAccountModule { }