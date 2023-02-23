package org.example.petclinic.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;
import org.example.petclinic.model.User;
import org.example.petclinic.persistence.UserPersistence;
import org.example.petclinic.service.impl.UserServiceImpl;

@WebApiServiceGen
public interface UserService {

  static UserServiceImpl create(UserPersistence userPersistence) {
    return new UserServiceImpl(userPersistence);
  }

  void addUser(User body, ServiceRequest request,
      Handler<AsyncResult<ServiceResponse>> resultHandler);
}
