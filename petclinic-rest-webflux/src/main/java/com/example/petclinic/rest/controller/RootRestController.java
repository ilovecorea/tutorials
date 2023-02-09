package com.example.petclinic.rest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Vitaliy Fedoriv
 */

@RestController
@RequestMapping("/")
public class RootRestController {

  @Value("${server.servlet.context-path")
  private String contextPath;

  @RequestMapping(value = "/")
  public Mono<String> redirectToSwagger() {
    return Mono.just("redirect:" + this.contextPath + "swagger-ui/index.html");
  }

}

