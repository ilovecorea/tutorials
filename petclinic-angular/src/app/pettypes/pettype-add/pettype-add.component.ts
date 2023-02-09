import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {PetType} from "../pettype";
import {PetTypeService} from "../pet-type.service";

@Component({
  selector: 'app-pettype-add',
  templateUrl: './pettype-add.component.html',
  styleUrls: ['./pettype-add.component.css']
})
export class PettypeAddComponent implements OnInit {
  pettype: PetType;
  errorMessage: string;
  @Output() newPetType = new EventEmitter<PetType>();

  constructor(private petTypeService: PetTypeService) {
    this.pettype = {} as PetType;
  }

  ngOnInit(): void {
  }

  onSubmit(pettype: PetType) {
    pettype.id = null;
    this.petTypeService.addPetType(pettype).subscribe(
      newPettype => {
        this.pettype = newPettype;
        this.newPetType.emit(this.pettype);
      },
      error => this.errorMessage = error as any
    );
  }
}
