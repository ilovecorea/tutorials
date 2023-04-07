package org.example.serverapp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.example.serverapp.model.Person;
import org.example.serverapp.service.PersonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PersonController {
	private final PersonService personService;

	@GetMapping("/add-temp")
	public ResponseEntity<Person> init() {
		var person = personService.addDumpData();
		return ResponseEntity.ok(person);
	}

	@PostMapping("/persons")
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		var result = personService.addPerson(person);
		return ResponseEntity.created(URI.create("/persons/" + result.getId())).body(result);
	}

	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAll() {
		return ResponseEntity.ok(personService.getApiList());
	}

	@GetMapping("/persons/{id}")
	public ResponseEntity<Person> get(@PathVariable("id") Long id) {
		var result = personService.get(id);
		return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/persons")
	public ResponseEntity<Void> deleteAll() {
		personService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/persons/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		personService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
