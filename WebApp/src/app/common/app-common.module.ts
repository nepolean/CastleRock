import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { HeaderComponent } from './page-sections/header/header.component';
import { MainBeginComponent } from './page-sections/main-begin/main-begin.component';
import { MainEndComponent } from './page-sections/main-end/main-end.component';
import { FooterComponent } from './page-sections/footer/footer.component';
import { DashboardComponent } from './page-sections/dashboard/dashboard.component';
import { AbstractPaginatorComponent } from './page-utils/paginator/abstract-paginator.component';

import { FormValidationMessageService } from './page-utils/form/form-validation-message.service';
import { ProfileService } from './services/profile.service';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  declarations: [
    HeaderComponent,
    MainBeginComponent,
    MainEndComponent,
    FooterComponent,
    DashboardComponent,
    AbstractPaginatorComponent
  ],
  exports: [
    HeaderComponent,
    MainBeginComponent,
    MainEndComponent,
    FooterComponent,
    DashboardComponent,
    AbstractPaginatorComponent
  ],
  providers: [
    FormValidationMessageService,
    ProfileService
  ]
})
export class AppCommonModule { }