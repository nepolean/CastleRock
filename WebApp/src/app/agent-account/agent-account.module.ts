import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppCommonModule } from '../common/app-common.module';
import { AgentAccountRoutingModule } from './agent-account-routing.module';
import { AgentAccountSideBarComponent } from './page-sections/sidebar/agent-account-sidebar.component';
import { AgentAccountPaginatorComponent } from './page-sections/paginator/agent-account-paginator.component';
import { AgentAccountComponent } from './agent-account.component';
import { UserProfileBeginAbstractComponent } from '../common/profile/user-profile-begin-abstract.component';
import { UserProfileEditComponent } from '../common/profile/user-profile-edit.component';
import { UserProfileChangePasswordComponent } from '../common/profile/user-profile-change-password.component';
import { MessageBeginAbstractComponent } from './home/message/message-begin-abstract.component';
import { HomeComponent } from './home/home.component';
import { MessageComponent } from './home/message/message.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    AgentAccountRoutingModule
  ],
  declarations: [
    AgentAccountPaginatorComponent,
    AgentAccountComponent,
    HomeComponent,
    UserProfileBeginAbstractComponent,
    UserProfileEditComponent,
    UserProfileChangePasswordComponent,
    MessageBeginAbstractComponent,
    MessageComponent,
    AgentAccountSideBarComponent
  ],
  providers: [
  ]
})
export class AgentAccountModule { }