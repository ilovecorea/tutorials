package org.example.petclinic.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.example.petclinic.model.Owner;
import org.example.petclinic.persistence.OwnersPersistence;
import org.example.petclinic.service.OwnersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OwnerServiceImpl implements OwnersService {

  private static final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

  private final OwnersPersistence ownersPersistence;

  public OwnerServiceImpl(OwnersPersistence ownersPersistence) {
    this.ownersPersistence = ownersPersistence;
  }

  @Override
  public void listOwners(String lastName, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    ownersPersistence.findAll()
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

  }

  @Override
  public void addOwner(Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }

  @Override
  public void updateOwner(Integer ownerId, Owner body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {

  }
}
