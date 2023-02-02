package com.example.petclinic.service;

import com.example.petclinic.model.Pet;
import com.example.petclinic.repository.OwnerRepository;
import com.example.petclinic.repository.PetRepository;
import com.example.petclinic.repository.PetTypeRepository;
import com.example.petclinic.repository.SpecialtyRepository;
import com.example.petclinic.repository.VetRepository;
import com.example.petclinic.repository.VisitRepository;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
public class ClinicService {

  private final PetRepository petRepository;
  private final VetRepository vetRepository;
  private final OwnerRepository ownerRepository;
  private final VisitRepository visitRepository;
  private final SpecialtyRepository specialtyRepository;
  private final PetTypeRepository petTypeRepository;

  public ClinicService(PetRepository petRepository, VetRepository vetRepository,
      OwnerRepository ownerRepository, VisitRepository visitRepository,
      SpecialtyRepository specialtyRepository, PetTypeRepository petTypeRepository) {
    this.petRepository = petRepository;
    this.vetRepository = vetRepository;
    this.ownerRepository = ownerRepository;
    this.visitRepository = visitRepository;
    this.specialtyRepository = specialtyRepository;
    this.petTypeRepository = petTypeRepository;
  }

  @Transactional(readOnly = true)
  public Flux<Pet> findAllPets() throws DataAccessException {
    return petRepository.findAll();
  }
}

