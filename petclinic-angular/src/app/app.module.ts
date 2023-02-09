import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {PetTypesModule} from "./pettypes/pettypes.module";
import {HttpErrorHandler} from "./error.service";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    PetTypesModule,
    AppRoutingModule
  ],
  providers: [
    HttpErrorHandler
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}
