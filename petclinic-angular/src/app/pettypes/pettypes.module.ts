import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PettypesRoutingModule} from './pettypes-routing.module';
import {PettypeListComponent} from './pettype-list/pettype-list.component';
import {FormsModule} from "@angular/forms";
import {PetTypeService} from "./pet-type.service";
import { PettypeEditComponent } from './pettype-edit/pettype-edit.component';
import { PettypeAddComponent } from './pettype-add/pettype-add.component';


@NgModule({
  declarations: [
    PettypeListComponent,
    PettypeEditComponent,
    PettypeAddComponent
  ],
  imports: [
    CommonModule,
    PettypesRoutingModule,
    FormsModule
  ],
  exports: [
    PettypeListComponent
  ],
  providers: [PetTypeService]
})
export class PetTypesModule {
}
