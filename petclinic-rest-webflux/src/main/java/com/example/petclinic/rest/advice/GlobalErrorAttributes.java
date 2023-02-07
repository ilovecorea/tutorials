package com.example.petclinic.rest.advice;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  private static final Logger log = LoggerFactory.getLogger(GlobalErrorAttributes.class);

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request,
      ErrorAttributeOptions options) {
    Map<String, Object> map = super.getErrorAttributes(request, options);
    Throwable throwable = getError(request);
    if (log.isDebugEnabled()) {
      log.debug("### Throwable:{}", throwable);
    }
    if (throwable instanceof EmptyResultDataAccessException) {
      map.put("status", HttpStatus.NOT_FOUND.value());
    } else {
      map.put("status", HttpStatus.BAD_REQUEST.value());
      map.put("message", throwable.getLocalizedMessage());
    }

    return map;
  }


}
