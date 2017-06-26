import { NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { NotFoundComponent } from './not-found.component';
import { AppRoutingModule } from './app-routing.module';
import { AppCommonModule } from './common/app-common.module';
import { AppAuthGuard } from './app-auth-guard.service';
import { AppAuthService } from './app-auth.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    AppCommonModule
  ],
  declarations: [
    AppComponent,
    NotFoundComponent
  ],
  providers: [
    AppAuthGuard,
    AppAuthService,
    Title
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }