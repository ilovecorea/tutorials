package com.example.petclinic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebExceptionHandler;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.spring.webflux.advice.ProblemExceptionHandler;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class WebConfiguration {

  @Bean
  public ProblemModule problemModule() {
    return new ProblemModule();
  }

  @Bean
  public ConstraintViolationProblemModule constraintViolationProblemModule() {
    return new ConstraintViolationProblemModule();
  }

  @Bean
  @Order(-2) // The handler must have precedence over WebFluxResponseStatusExceptionHandler and Spring Boot's ErrorWebExceptionHandler
  public WebExceptionHandler problemExceptionHandler(ObjectMapper mapper, ProblemHandling problemHandling) {
    return new ProblemExceptionHandler(mapper, problemHandling);
  }

  @Bean
  @Primary
  public ObjectMapper objectMapper(ProblemModule problem) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(problem);
    return objectMapper;
  }

  @Bean
  public Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper mapper) {
    return new Jackson2JsonEncoder(mapper);
  }

  @Bean
  public Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper mapper) {
    return new Jackson2JsonDecoder(mapper);
  }

  @Bean
  public WebFluxConfigurer webFluxConfigurer(final Jackson2JsonEncoder encoder,
      final Jackson2JsonDecoder decoder) {
    return new WebFluxConfigurer() {

      public void configureHttpMessageCodec(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(encoder);
        configurer.defaultCodecs().jackson2JsonDecoder(decoder);
      }
    };
  }
}
