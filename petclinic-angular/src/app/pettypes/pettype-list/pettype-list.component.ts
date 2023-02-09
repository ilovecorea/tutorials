import { Component, OnInit } from '@angular/core';
import {PetType} from "../pettype";
import {Router} from "@angular/router";
import {PetTypeService} from "../pet-type.service";

@Component({
  selector: 'app-pettype-list',
  templateUrl: './pettype-list.component.html',
  styleUrls: ['./pettype-list.component.css']
})
export class PettypeListComponent implements OnInit {

  pettypes: PetType[];
  errorMessage: string;
  responseStatus: number;
  isInsert = false;

  constructor(private petTypeService: PetTypeService, private router: Router) {
    this.pettypes = [] as PetType[];
  }

  ngOnInit(): void {
    this.petTypeService.getPetTypes().subscribe(
      pettypes => this.pettypes = pettypes,
      error => this.errorMessage = error as any
    );
  }

  deletePettype(pettype: PetType) {

  }

  onNewPettype($event: any) {

  }

  showAddPettypeComponent() {
    this.isInsert = !this.isInsert;
  }

  showEditPettypeComponent(updatedPetType: PetType) {
    this.router.navigate(['/pettypes', updatedPetType.id.toString(), 'edit'])
  }

  gotoHome() {
    this.router.navigate(['/welcome']);
  }
}
