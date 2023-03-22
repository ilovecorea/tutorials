package org.example.petclinic.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(
      RestResponseEntityExceptionHandler.class);

  /**
   * javax.validation 에러 처리
   */
  @ExceptionHandler(value = {javax.validation.ConstraintViolationException.class})
  public ResponseEntity handleConstraintViolation(javax.validation.ConstraintViolationException ex,
      WebRequest request) {
    List<String> messages = ex.getConstraintViolations().stream()
        .map(constraintViolation -> constraintViolation.getMessage())
        .toList();
    log.info("## messages:{}", messages);
    return handleExceptionInternal(ex, String.join(",", messages), new HttpHeaders(),
        HttpStatus.BAD_REQUEST, request);
  }

  /**
   * 에러 응답 헨들러 재정의
   * @param ex
   * @param body
   * @param headers
   * @param status
   * @param request
   * @return
   */
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    Map<String, Object> errorAttributes = new HashMap<>();
    errorAttributes.put("timestamp", new Date());
    errorAttributes.put("status", status.value());
    errorAttributes.put("error", status.getReasonPhrase());
    errorAttributes.put("message", body);
    return ResponseEntity.status(status).body(errorAttributes);
  }
}
