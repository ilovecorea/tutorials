import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PersonModel} from "./model/person.model";
import {Observable} from "rxjs";
import {ConfigModel} from "./model/config.model";
import {config} from "./config/config";

@Injectable({
  providedIn: 'root'
})
export class PersonService {

  constructor(private http: HttpClient) {
  }

  private config: ConfigModel = config

  getPersons(): Observable<PersonModel[]> {
    return this.http.get<PersonModel[]>(`${this.config.apiBaseUrl}/persons`);
  }

  getPerson(id: number): Observable<PersonModel> {
    return this.http.get<PersonModel>(`${this.config.apiBaseUrl}/persons/${id}`);
  }

  addTemp(): Observable<PersonModel> {
    return this.http.get<PersonModel>(`${this.config.apiBaseUrl}/add-temp`);
  }

  deleteAll(): Observable<void> {
    return this.http.delete<void>(`${this.config.apiBaseUrl}/persons`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.config.apiBaseUrl}/persons/${id}`);
  }

}
