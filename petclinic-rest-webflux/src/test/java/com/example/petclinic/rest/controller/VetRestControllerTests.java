package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.VetMapper;
import com.example.petclinic.mapper.VetMapperImpl;
import com.example.petclinic.model.Vet;
import com.example.petclinic.rest.advice.GlobalErrorAttributes;
import com.example.petclinic.rest.advice.GlobalErrorExceptionHandler;
import com.example.petclinic.service.ClinicService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(VetRestController.class)
@ContextConfiguration(classes = {
    ApplicationTestConfig.class
})
@Import(value = {
    GlobalErrorAttributes.class,
    GlobalErrorExceptionHandler.class,
    VetMapperImpl.class
})
public class VetRestControllerTests {

  @MockBean
  protected ClinicService clinicService;

  @Autowired
  private VetMapper vetMapper;

  @Autowired
  WebTestClient webTestClient;

  private List<Vet> vets;

  @BeforeEach
  void initVets(){
    vets = new ArrayList<Vet>();

    Vet vet = new Vet();
    vet.setId(1);
    vet.setFirstName("James");
    vet.setLastName("Carter");
    vets.add(vet);

    vet = new Vet();
    vet.setId(2);
    vet.setFirstName("Helen");
    vet.setLastName("Leary");
    vets.add(vet);

    vet = new Vet();
    vet.setId(3);
    vet.setFirstName("Linda");
    vet.setLastName("Douglas");
    vets.add(vet);
  }
}
