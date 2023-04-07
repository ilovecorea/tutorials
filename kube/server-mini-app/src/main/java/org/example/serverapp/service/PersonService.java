package org.example.serverapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import org.example.serverapp.model.Person;
import org.example.serverapp.repository.PersonRepository;
import org.example.serverapp.util.NameUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonService {

	private final PersonRepository personRepository;

	public Person addDumpData() {
		var random = new Random();

		var person = new Person();
		person.setAge(random.nextInt(20, 30));
		person.setName(NameUtils.getRandomName());
		return personRepository.save(person);
	}

	public List<Person> getApiList() {
		return personRepository.findAll();
	}
	public Optional<Person> get(Long id) {
		return personRepository.findById(id);
	}

	public Person addPerson(Person person) {
		return personRepository.save(person);
	}

	public void deleteAll() {
		personRepository.deleteAll();
	}
	public void delete(Long id) {
		personRepository.deleteById(id);
	}
}
