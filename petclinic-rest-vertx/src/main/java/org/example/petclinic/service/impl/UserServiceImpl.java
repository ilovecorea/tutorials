package org.example.petclinic.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import org.example.petclinic.model.User;
import org.example.petclinic.persistence.UserPersistence;
import org.example.petclinic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {

  private final UserPersistence userPersistence;

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  public UserServiceImpl(UserPersistence userPersistence) {
    this.userPersistence = userPersistence;
  }

  @Override
  public void addUser(User body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler) {
    log.debug("## body:{}", body);
    userPersistence.add(body)
        .onSuccess(user -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse().setStatusCode(201))))
        .onFailure(e -> resultHandler.handle(
            Future.succeededFuture(new ServiceResponse()
                .setStatusCode(500)
                .setStatusMessage(e.getMessage()))));
  }
}
