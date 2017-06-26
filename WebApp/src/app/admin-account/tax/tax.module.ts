import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppCommonModule } from '../../common/app-common.module';
import { TaxRoutingModule } from './tax-routing.module';
import { TaxComponent } from './tax.component';
import { TaxAbstractFormComponent } from './tax-abstract-form.component';
import { TaxDetailComponent } from './tax-detail.component';
import { TaxFieldsDirective } from './tax-fields-directive.component';
import { TaxActionButtonsDirective } from './tax-action-buttons-directive.component';
import { TaxActionModalsDirective } from './tax-action-modals-directive.component';
import { TaxPaginatorComponent } from './tax-paginator.component';
import { TaxEditComponent } from './tax-edit.component';
import { TaxAddComponent } from './tax-add.component';
import { TaxService } from './mock-tax.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    TaxRoutingModule
  ],
  declarations: [
    TaxComponent,
    TaxAbstractFormComponent,
    TaxFieldsDirective,
    TaxActionButtonsDirective,
    TaxActionModalsDirective,
    TaxPaginatorComponent,
    TaxDetailComponent,
    TaxAddComponent,
    TaxEditComponent
  ],
  providers: [
    TaxService
  ]
})
export class TaxModule { }