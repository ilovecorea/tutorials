package com.example.petclinic.rest.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Vitaliy Fedoriv
 */

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/")
public class RootRestController {

  @Value("${server.servlet.context-path")
  private String contextPath;

  @RequestMapping(value = "/")
  public Mono<String> redirectToSwagger() {
    return Mono.just("redirect:" + this.contextPath + "swagger-ui/index.html");
  }

}

