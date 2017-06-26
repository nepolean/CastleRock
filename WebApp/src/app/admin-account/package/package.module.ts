import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppCommonModule } from '../../common/app-common.module';
import { PackageRoutingModule } from './package-routing.module';
import { PackageComponent } from './package.component';
import { PackageAbstractFormComponent } from './package-abstract-form.component';
import { PackageDetailComponent } from './package-detail.component';
import { PackageFieldsDirective } from './package-fields-directive.component';
import { PackageActionButtonsDirective } from './package-action-buttons-directive.component';
import { PackageActionModalsDirective } from './package-action-modals-directive.component';
import { PackagePaginatorComponent } from './package-paginator.component';
import { PackageEditComponent } from './package-edit.component';
import { PackageAddComponent } from './package-add.component';
import { PackageService } from './mock-package.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    PackageRoutingModule
  ],
  declarations: [
    PackageComponent,
    PackageAbstractFormComponent,
    PackageFieldsDirective,
    PackageActionButtonsDirective,
    PackageActionModalsDirective,
    PackagePaginatorComponent,
    PackageDetailComponent,
    PackageAddComponent,
    PackageEditComponent
  ],
  providers: [
    PackageService
  ]
})
export class PackageModule { }