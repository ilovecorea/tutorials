package com.example.petclinic.service;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.repository.OwnerRepository;
import com.example.petclinic.repository.PetRepository;
import com.example.petclinic.repository.PetTypeRepository;
import com.example.petclinic.repository.SpecialtyRepository;
import com.example.petclinic.repository.VetRepository;
import com.example.petclinic.repository.VisitRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClinicService {

  private final PetRepository petRepository;
  private final VetRepository vetRepository;
  private final OwnerRepository ownerRepository;
  private final VisitRepository visitRepository;
  private final SpecialtyRepository specialtyRepository;
  private final PetTypeRepository petTypeRepository;

  private final DatabaseClient databaseClient;

  public ClinicService(PetRepository petRepository,
      VetRepository vetRepository,
      OwnerRepository ownerRepository,
      VisitRepository visitRepository,
      SpecialtyRepository specialtyRepository,
      PetTypeRepository petTypeRepository,
      DatabaseClient databaseClient) {
    this.petRepository = petRepository;
    this.vetRepository = vetRepository;
    this.ownerRepository = ownerRepository;
    this.visitRepository = visitRepository;
    this.specialtyRepository = specialtyRepository;
    this.petTypeRepository = petTypeRepository;
    this.databaseClient = databaseClient;
  }

  @Transactional(readOnly = true)
  public Flux<Pet> findAllPets() throws DataAccessException {
    return petRepository.findAll();
  }

  public Flux<Owner> findOwnerByLastName(String lastName) {
    return ownerRepository.findByLastName(lastName);
  }

  public Mono<Owner> findOwnerById(int id) {
    return ownerRepository.findWithPetsById(databaseClient, id)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)));
  }

  @Transactional
  public Mono<Owner> saveOwner(Owner owner) {
    return ownerRepository.save(owner);
  }

  public Mono<Pet> findPetById(int id) {
    return petRepository.findPetWithOwnerById(databaseClient, id)
        .switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1)));
  }
}

