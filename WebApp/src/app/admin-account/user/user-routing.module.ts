import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserComponent } from './user.component';
import { UserAddComponent } from './user-add.component';
import { UserDetailComponent } from './user-detail.component';
import { UserEditComponent } from './user-edit.component';

const routes: Routes = [
  { path: '', component: UserComponent },
  { path: 'add', component: UserAddComponent },
  { path: 'edit/:username', component: UserEditComponent },
  { path: 'view/:username', component: UserDetailComponent }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class UserRoutingModule {}