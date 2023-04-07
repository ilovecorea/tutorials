import {Component, OnInit} from '@angular/core';
import {PersonModel} from "./model/person.model";
import {PersonService} from "./person.service";
import {ConfigModel} from "./model/config.model";
import {config} from "./config/config";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  constructor(private service: PersonService) {
  }

  config: ConfigModel = config

  title = 'example';
  persons: PersonModel[] = [];
  modiPerson: PersonModel | null = null;

  ngOnInit(): void {
    this.dataRefresh()
  }

  dataRefresh(): void {
    this.service.getPersons()
      .subscribe((persons: PersonModel[]) => this.persons = persons)
  }

  getDetails(id: number): void {
    this.service.getPerson(id)
      .subscribe((person: PersonModel) => this.modiPerson = person)
  }

  addItem(): void {
    this.service.addTemp()
      .subscribe((person: PersonModel) => this.persons.push(person))
  }

  deleteAll(): void {
    this.service.deleteAll()
      .subscribe(() => this.persons = [])
  }
  delete(id: number): void {
    this.service.delete(id)
      .subscribe(() => this.dataRefresh())
  }

}
