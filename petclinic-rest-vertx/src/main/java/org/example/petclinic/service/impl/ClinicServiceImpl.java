package org.example.petclinic.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import java.util.stream.Collectors;
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
import org.example.petclinic.service.ClinicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClinicServiceImpl implements ClinicService {

  private final OwnersPersistence ownersPersistence;
  private final PetPersistence petPersistence;
  private final PetTypePersistence petTypePersistence;
  private final VetPersistence vetPersistence;
  private final VisitPersistence visitPersistence;
  private final SpecialtyPersistence specialtyPersistence;

  private static final Logger log = LoggerFactory.getLogger(ClinicServiceImpl.class);

  public ClinicServiceImpl(OwnersPersistence ownersPersistence, PetPersistence petPersistence,
      PetTypePersistence petTypePersistence, VetPersistence vetPersistence,
      VisitPersistence visitPersistence, SpecialtyPersistence specialtyPersistence) {
    this.ownersPersistence = ownersPersistence;
    this.petPersistence = petPersistence;
    this.petTypePersistence = petTypePersistence;
    this.vetPersistence = vetPersistence;
    this.visitPersistence = visitPersistence;
    this.specialtyPersistence = specialtyPersistence;
  }

  @Override
  public void addOwner(Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    ownersPersistence.add(body)
        .onSuccess(owner -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(500))));
  }

  @Override
  public void listOwners(String lastName, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    ownersPersistence.findByLastName(lastName)
        .onSuccess(owners -> {
          log.debug("## owners:{}", owners);
          if (owners.isEmpty()) {
            resultHandler.handle(Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
          } else {
            resultHandler.handle(
                Future.succeededFuture(
                    ServiceResponse.completedWithJson(
                        new JsonArray(
                            owners.stream().map(Owner::toJson).collect(Collectors.toList())))));
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(500))));
  }

  @Override
  public void getOwner(Integer ownerId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    ownersPersistence.findById(ownerId)
        .onSuccess(owner -> {
          if (owner.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(ServiceResponse.completedWithJson(owner.get().toJson())));
          } else {
            resultHandler.handle(Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(500))));
  }

  @Override
  public void updateOwner(Integer ownerId, Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setId(ownerId);
    ownersPersistence.save(body)
        .onSuccess(owner -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(500))));
  }

  @Override
  public void deleteOwner(Integer ownerId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    ownersPersistence.remove(ownerId);
  }

  @Override
  public void addPetToOwner(Integer ownerId, Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setOwnerId(ownerId);
    body.setTypeId(body.getType().getId());
    petPersistence.add(body)
        .onSuccess(result -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(500))));
  }

  @Override
  public void getOwnersPet(Integer ownerId, Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petPersistence.findById(petId)
        .onSuccess(pet -> {
          if (pet.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(200))
            );
          } else {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(404))
            );
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse().setStatusCode(500).setStatusMessage(e.getMessage())
            )
        ));
  }

  @Override
  public void updateOwnersPet(Integer ownerId, Integer petId, Pet pet, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    log.debug("## pet:{}", pet);
    petPersistence.save(pet)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))
        ))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse().setStatusCode(500).setStatusMessage(e.getMessage())
            )
        ));
  }

  @Override
  public void addVisitToOwner(Integer ownerId, Integer petId, Visit visit, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void listPetTypes(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void addPetType(PetType body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void getPetType(Integer petTypeId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void updatePetType(Integer petTypeId, PetType body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void deletePetType(Integer petTypeId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void listPets(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void addPet(Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void getPet(Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void updatePet(Integer petId, Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void deletePet(Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void listVisits(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void addVisit(Visit body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void getVisit(Integer visitId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void updateVisit(Integer visitId, Visit body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void deleteVisit(Integer visitId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void listSpecialties(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void addSpecialty(Specialty body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void getSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void updateSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void deleteSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void listVets(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void addVet(Vet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void getVet(Integer vetId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void updateVet(Integer vetId, Vet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void deleteVet(Integer vetId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }
}
