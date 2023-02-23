package org.example.petclinic.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;
import org.example.petclinic.model.Owner;
import org.example.petclinic.model.Pet;
import org.example.petclinic.model.PetType;
import org.example.petclinic.model.Specialty;
import org.example.petclinic.model.Vet;
import org.example.petclinic.model.Visit;
import org.example.petclinic.persistence.OwnersPersistence;
import org.example.petclinic.persistence.PetPersistence;
import org.example.petclinic.persistence.PetTypePersistence;
import org.example.petclinic.persistence.SpecialtyPersistence;
import org.example.petclinic.persistence.VetPersistence;
import org.example.petclinic.persistence.VisitPersistence;
import org.example.petclinic.service.impl.ClinicServiceImpl;

@WebApiServiceGen
public interface ClinicService {

  static ClinicServiceImpl create(
      OwnersPersistence ownersPersistence,
      PetPersistence petPersistence,
      PetTypePersistence petTypePersistence,
      VetPersistence vetPersistence,
      VisitPersistence visitPersistence,
      SpecialtyPersistence specialtyPersistence) {
    return new ClinicServiceImpl(ownersPersistence, petPersistence, petTypePersistence,
        vetPersistence, visitPersistence, specialtyPersistence);
  }

  void addOwner(Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void listOwners(String lastName, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getOwner(Integer ownerId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateOwner(Integer ownerId, Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteOwner(Integer ownerId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addPetToOwner(Integer ownerId, Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getOwnersPet(Integer ownerId, Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateOwnersPet(Integer ownerId, Integer petId, Pet pet, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addVisitToOwner(Integer ownerId, Integer petId, Visit visit, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void listPetTypes(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addPetType(PetType body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getPetType(Integer petTypeId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updatePetType(Integer petTypeId, PetType body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deletePetType(Integer petTypeId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void listPets(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addPet(Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getPet(Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updatePet(Integer petId, Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deletePet(Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void listVisits(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addVisit(Visit body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getVisit(Integer visitId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateVisit(Integer visitId, Visit body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteVisit(Integer visitId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void listSpecialties(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addSpecialty(Specialty body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void listVets(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addVet(Vet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getVet(Integer vetId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateVet(Integer vetId, Vet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteVet(Integer vetId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);
}
