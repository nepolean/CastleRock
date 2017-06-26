import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppCommonModule } from '../../common/app-common.module';
import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './user.component';
import { UserAbstractFormComponent } from './user-abstract-form.component';
import { UserDetailComponent } from './user-detail.component';
import { UserProfileFieldsDirective } from './user-profile-fields-directive.component';
import { UserActionButtonsDirective } from './user-action-buttons-directive.component';
import { UserActionModalsDirective } from './user-action-modals-directive.component';
import { UserPaginatorComponent } from './user-paginator.component';
import { UserEditComponent } from './user-edit.component';
import { UserAddComponent } from './user-add.component';
import { UserService } from './user.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    UserRoutingModule
  ],
  declarations: [
    UserComponent,
    UserAbstractFormComponent,
    UserProfileFieldsDirective,
    UserActionButtonsDirective,
    UserActionModalsDirective,
    UserPaginatorComponent,
    UserDetailComponent,
    UserAddComponent,
    UserEditComponent
  ],
  providers: [
    UserService
  ]
})
export class UserModule { }