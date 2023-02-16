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
import org.example.petclinic.model.Visit;
import org.example.petclinic.persistence.OwnersPersistence;
import org.example.petclinic.persistence.PetPersistence;
import org.example.petclinic.service.OwnersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OwnerServiceImpl implements OwnersService {

  private static final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

  private final OwnersPersistence ownersPersistence;

  private final PetPersistence petPersistence;

  public OwnerServiceImpl(
      OwnersPersistence ownersPersistence,
      PetPersistence petPersistence) {
    this.ownersPersistence = ownersPersistence;
    this.petPersistence = petPersistence;
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
        .onFailure(e -> Future.succeededFuture(new ServiceResponse().setStatusCode(500)));
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
        .onFailure(e -> Future.succeededFuture(new ServiceResponse().setStatusCode(500)));
  }

  @Override
  public void addOwner(Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    ownersPersistence.add(body)
        .onSuccess(owner -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> Future.succeededFuture(new ServiceResponse().setStatusCode(500)));
  }

  @Override
  public void updateOwner(Integer ownerId, Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    body.setId(ownerId);
    ownersPersistence.save(body)
        .onSuccess(owner -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(204))))
        .onFailure(e -> Future.succeededFuture(new ServiceResponse().setStatusCode(500)));
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
        .onFailure(e -> Future.succeededFuture(new ServiceResponse().setStatusCode(500)));
  }

  @Override
  public void getOwnersPet(Integer ownerId, Integer petId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void updateOwnersPet(Integer ownerId, Integer petId, Pet pet, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void addVisitToOwner(Integer ownerId, Integer petId, Visit visit, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }
}
