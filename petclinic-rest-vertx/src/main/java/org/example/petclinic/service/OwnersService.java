package org.example.petclinic.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;
import org.example.petclinic.model.Owner;
import org.example.petclinic.model.Pet;
import org.example.petclinic.model.Visit;
import org.example.petclinic.persistence.OwnersPersistence;
import org.example.petclinic.persistence.PetPersistence;
import org.example.petclinic.service.impl.OwnerServiceImpl;

@WebApiServiceGen
public interface OwnersService {

  static OwnerServiceImpl create(
      OwnersPersistence ownersPersistence,
      PetPersistence petPersistence) {
    return new OwnerServiceImpl(ownersPersistence, petPersistence);
  }

  void listOwners(String lastName, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getOwner(Integer ownerId, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);

  void addOwner(Owner body, ServiceRequest request,
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
}
