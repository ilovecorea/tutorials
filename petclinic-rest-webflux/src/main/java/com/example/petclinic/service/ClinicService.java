package com.example.petclinic.service;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import com.example.petclinic.model.Specialty;
import com.example.petclinic.model.Vet;
import com.example.petclinic.model.Visit;
import com.example.petclinic.repository.OwnerRepository;
import com.example.petclinic.repository.PetRepository;
import com.example.petclinic.repository.PetTypeRepository;
import com.example.petclinic.repository.SpecialtyRepository;
import com.example.petclinic.repository.VetRepository;
import com.example.petclinic.repository.VisitRepository;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
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
  private static final Logger log = LoggerFactory.getLogger(ClinicService.class);

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
  public Flux<Pet> findAllPets() {
    return petRepository.findAll();
  }

  @Transactional
  public Mono<Void> deletePet(Pet pet) {
    return petRepository.delete(pet);
  }

  public Mono<Visit> findVisitById(int visitId) {
    return visitRepository.findById(databaseClient, visitId);
  }

  public Flux<Visit> findAllVisits() {
    return visitRepository.findAll(databaseClient);
  }

  public Mono<Void> deleteVisit(Visit visit) {
    return visitRepository.delete(visit);
  }

  public Mono<Vet> findVetById(int id) {
    return vetRepository.findById(id);
  }

  public Flux<Vet> findAllVets() {
    return vetRepository.findAll();
  }

  @Transactional
  public Mono<Vet> saveVet(Vet vet) throws DataAccessException {
    return vetRepository.save(vet);
  }

  public Mono<Void> deleteVet(Vet vet) {
    return vetRepository.delete(vet);
  }

  public Flux<Owner> findAllOwners() {
    return ownerRepository.findAll();
  }

  @Transactional
  public Mono<Void> deleteOwner(Owner owner) {
    return petRepository.findByOwnerId(owner.getId())
        .collectList()
        .flatMap(pets -> {
          log.debug("## pets:{}", pets);
          if (pets != null && !pets.isEmpty()) {
            return petRepository.deleteAll(pets)
                .then(Mono.defer(() -> ownerRepository.delete(owner)));
          } else {
            return ownerRepository.delete(owner);
          }
        });
  }

  public Mono<PetType> findPetTypeById(int petTypeId) {
    return petTypeRepository.findById(petTypeId);
  }

  public Flux<PetType> findAllPetTypes() {
    return petTypeRepository.findAll();
  }

  @Transactional
  public Mono<PetType> savePetType(PetType petType) {
    return petTypeRepository.save(petType);
  }

  @Transactional
  public Mono<Void> deletePetType(PetType petType) {
    return petRepository.findByTypeId(petType.getId())
        .collectList()
        .flatMap(pets -> {
          log.debug("## pets:{}", pets);
          if (pets != null && !pets.isEmpty()) {
            return visitRepository.deleteByPetIdIn(
                    pets.stream()
                        .map(pet -> pet.getId())
                        .collect(Collectors.toSet()))
                .then(Mono.defer(() -> petRepository.deleteAll(pets)))
                .then(Mono.defer(() -> petTypeRepository.delete(petType)));
          } else {
            return petTypeRepository.delete(petType);
          }
        });
  }

  public Mono<Specialty> findSpecialtyById(int specialtyId) {
    return specialtyRepository.findById(specialtyId);
  }

  public Flux<Specialty> findAllSpecialties() {
    return specialtyRepository.findAll();
  }

  @Transactional
  public Mono<Specialty> saveSpecialty(Specialty specialty) {
    return specialtyRepository.save(specialty);
  }

  @Transactional
  public Mono<Void> deleteSpecialty(Specialty specialty) throws DataAccessException {
    return specialtyRepository.delete(specialty);
  }

  public Flux<PetType> findPetTypes() {
    return petTypeRepository.findAll(Sort.by("name"));
  }

  public Mono<Owner> findOwnerById(int id) {
    return ownerRepository.findById(id)
        .flatMap(owner -> petRepository.findByOwnerId(databaseClient, id)
            .collectList()
            .map(pets -> {
              owner.setPets(pets);
              return owner;
            }));
  }

  public Mono<Pet> findPetById(int id) {
    return petRepository.findById(databaseClient, id)
        .flatMap(pet -> visitRepository.findByPetId(pet.getId())
            .collectList()
            .map(visits -> {
              pet.setVisits(visits);
              return pet;
            }));
  }

  @Transactional
  public Mono<Pet> savePet(Pet pet) {
    if (pet.getVisits() == null) {
      return petRepository.save(pet)
          .onErrorResume(e -> Mono.error(new DataServiceException("savePet", e)));
    } else {
      return visitRepository.saveAll(pet.getVisits())
          .then(petRepository.save(pet));
    }
  }


  @Transactional
  public Mono<Visit> saveVisit(Visit visit) {
    return this.visitRepository.save(visit);
  }

  public Flux<Vet> findVets() {
    return vetRepository.findAllWithSpecialties(databaseClient);
  }

  @Transactional
  public Mono<Owner> saveOwner(Owner owner) {
    Objects.requireNonNull(owner);
    if (log.isDebugEnabled()) {
      log.debug("owner:{}", owner);
    }
    if (owner.getPets() != null) {
      return petRepository.saveAll(owner.getPets())
          .then(ownerRepository.save(owner));
    } else {
      return ownerRepository.save(owner);
    }
  }

  public Flux<Owner> findOwnerByLastName(String lastName) {
    return ownerRepository.findByLastName(lastName);
  }

  public Flux<Visit> findVisitsByPetId(int petId) {
    return visitRepository.findByPetId(databaseClient, petId);
  }

}

