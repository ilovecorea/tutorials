package com.example.petclinic.rest.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.spring.webflux.advice.validation.OpenApiValidationAdviceTrait;

@ControllerAdvice
public class ExceptionControllerAdvice implements ProblemHandling , OpenApiValidationAdviceTrait {

}
