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
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
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
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
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
        .onFailure(e -> resultHandler.handle(Future.succeededFuture(new ServiceResponse()
            .setStatusCode(500)
            .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void updateOwner(Integer ownerId, Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setId(ownerId);
    ownersPersistence.save(body)
        .onSuccess(owner -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(200))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void deleteOwner(Integer ownerId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    ownersPersistence.remove(ownerId)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(200))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
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
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void getOwnersPet(Integer ownerId, Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petPersistence.findById(petId)
        .onSuccess(pet -> {
          if (pet.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(ServiceResponse.completedWithJson(pet.get().toJson()))
            );
          } else {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(404))
            );
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage())
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
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void addVisitToOwner(Integer ownerId, Integer petId, Visit visit, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    visitPersistence.add(visit)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))
        ))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void listPetTypes(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petTypePersistence.findAll()
        .onSuccess(petType -> {
              if (petType.isEmpty()) {
                resultHandler.handle(
                    Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
              } else {
                resultHandler.handle(
                    Future.succeededFuture(ServiceResponse.completedWithJson(
                        new JsonArray(
                            petType.stream().map(PetType::toJson).collect(Collectors.toList())))));
              }
            }
        ).onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void addPetType(PetType body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petTypePersistence.add(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void getPetType(Integer petTypeId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petTypePersistence.findById(petTypeId)
        .onSuccess(petType -> {
          if (petType.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(ServiceResponse.completedWithJson(petType.get().toJson())));
          } else {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage())
            )
        ));
  }

  @Override
  public void updatePetType(Integer petTypeId, PetType body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petTypePersistence.save(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))
        ))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void deletePetType(Integer petTypeId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petTypePersistence.remove(petTypeId)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(200))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void listPets(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petPersistence.findAll()
        .onSuccess(pets -> {
              if (pets.isEmpty()) {
                resultHandler.handle(
                    Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
              } else {
                resultHandler.handle(
                    Future.succeededFuture(ServiceResponse.completedWithJson(
                        new JsonArray(
                            pets.stream().map(Pet::toJson).collect(Collectors.toList())))));
              }
            }
        ).onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void addPet(Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petPersistence.add(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void getPet(Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petPersistence.findById(petId)
        .onSuccess(pet -> {
          if (pet.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(ServiceResponse.completedWithJson(pet.get().toJson())));
          } else {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage())
            )
        ));
  }

  @Override
  public void updatePet(Integer petId, Pet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setId(petId);
    petPersistence.save(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))
        ))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void deletePet(Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    petPersistence.remove(petId)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(200))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void listVisits(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    visitPersistence.findAll()
        .onSuccess(visits -> {
              if (visits.isEmpty()) {
                resultHandler.handle(
                    Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
              } else {
                resultHandler.handle(
                    Future.succeededFuture(ServiceResponse.completedWithJson(
                        new JsonArray(
                            visits.stream().map(Visit::toJson).collect(Collectors.toList())))));
              }
            }
        ).onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void addVisit(Visit body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    visitPersistence.add(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void getVisit(Integer visitId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    visitPersistence.findById(visitId)
        .onSuccess(visit -> {
          if (visit.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(ServiceResponse.completedWithJson(visit.get().toJson())));
          } else {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage())
            )
        ));
  }

  @Override
  public void updateVisit(Integer visitId, Visit body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setId(visitId);
    visitPersistence.save(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))
        ))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void deleteVisit(Integer visitId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    visitPersistence.remove(visitId)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(200))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void listSpecialties(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    specialtyPersistence.findAll()
        .onSuccess(specialties -> {
              if (specialties.isEmpty()) {
                resultHandler.handle(
                    Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
              } else {
                resultHandler.handle(
                    Future.succeededFuture(ServiceResponse.completedWithJson(
                        new JsonArray(
                            specialties.stream().map(Specialty::toJson).collect(Collectors.toList())))));
              }
            }
        ).onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void addSpecialty(Specialty body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    specialtyPersistence.add(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void getSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    specialtyPersistence.findById(specialtyId)
        .onSuccess(specialty -> {
          if (specialty.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(ServiceResponse.completedWithJson(specialty.get().toJson())));
          } else {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage())
            )
        ));
  }

  @Override
  public void updateSpecialty(Integer specialtyId, Specialty body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setId(specialtyId);
    specialtyPersistence.save(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))
        ))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void deleteSpecialty(Integer specialtyId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    specialtyPersistence.remove(specialtyId)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(200))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void listVets(ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    vetPersistence.findAll()
        .onSuccess(vets -> {
              if (vets.isEmpty()) {
                resultHandler.handle(
                    Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
              } else {
                resultHandler.handle(
                    Future.succeededFuture(ServiceResponse.completedWithJson(
                        new JsonArray(
                            vets.stream().map(Vet::toJson).collect(Collectors.toList())))));
              }
            }
        ).onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void addVet(Vet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    vetPersistence.add(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }

  @Override
  public void getVet(Integer vetId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    vetPersistence.findById(vetId)
        .onSuccess(vet -> {
          if (vet.isPresent()) {
            resultHandler.handle(
                Future.succeededFuture(ServiceResponse.completedWithJson(vet.get().toJson())));
          } else {
            resultHandler.handle(
                Future.succeededFuture(new ServiceResponse().setStatusCode(404)));
          }
        })
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage())
            )
        ));
  }

  @Override
  public void updateVet(Integer vetId, Vet body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setId(vetId);
    vetPersistence.save(body)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))
        ))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(
                new ServiceResponse()
                    .setStatusCode(500)
                    .setStatusMessage(e.getMessage()))
        ));
  }

  @Override
  public void deleteVet(Integer vetId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    vetPersistence.remove(vetId)
        .onSuccess(r -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(200))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))
        ));
  }
}
