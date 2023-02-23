package com.example.petclinic.rest.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.petclinic.mapper.PetTypeMapperImpl;
import com.example.petclinic.mapper.SpecialtyMapper;
import com.example.petclinic.mapper.SpecialtyMapperImpl;
import com.example.petclinic.model.Specialty;
import com.example.petclinic.rest.advice.ExceptionControllerAdvice;
import com.example.petclinic.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {
    ApplicationTestConfig.class,
    SpecialtyMapperImpl.class
})
@Import(value = {SpecialtyRestController.class, ExceptionControllerAdvice.class})
@WebMvcTest
public class SpecialtyRestControllerTests {

  @Autowired
  private SpecialtyMapper specialtyMapper;

  @MockBean
  private ClinicService clinicService;

  @Autowired
  private MockMvc mockMvc;

  private List<Specialty> specialties;

  @BeforeEach
  void initSpecialtys(){
    specialties = new ArrayList<Specialty>();

    Specialty specialty = new Specialty();
    specialty.setId(1);
    specialty.setName("radiology");
    specialties.add(specialty);

    specialty = new Specialty();
    specialty.setId(2);
    specialty.setName("surgery");
    specialties.add(specialty);

    specialty = new Specialty();
    specialty.setId(3);
    specialty.setName("dentistry");
    specialties.add(specialty);

  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetSpecialtySuccess() throws Exception {
    given(this.clinicService.findSpecialtyById(1)).willReturn(specialties.get(0));
    this.mockMvc.perform(get("/api/specialties/1")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("radiology"));
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetSpecialtyNotFound() throws Exception {
    given(this.clinicService.findSpecialtyById(999)).willReturn(null);
    this.mockMvc.perform(get("/api/specialties/999")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetAllSpecialtysSuccess() throws Exception {
    specialties.remove(0);
    given(this.clinicService.findAllSpecialties()).willReturn(specialties);
    this.mockMvc.perform(get("/api/specialties/")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.[0].id").value(2))
        .andExpect(jsonPath("$.[0].name").value("surgery"))
        .andExpect(jsonPath("$.[1].id").value(3))
        .andExpect(jsonPath("$.[1].name").value("dentistry"));
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testGetAllSpecialtysNotFound() throws Exception {
    specialties.clear();
    given(this.clinicService.findAllSpecialties()).willReturn(specialties);
    this.mockMvc.perform(get("/api/specialties/")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testCreateSpecialtySuccess() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    newSpecialty.setId(999);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(specialtyMapper.toSpecialtyDto(newSpecialty));
    this.mockMvc.perform(post("/api/specialties/")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testCreateSpecialtyError() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    newSpecialty.setId(null);
    newSpecialty.setName(null);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(specialtyMapper.toSpecialtyDto(newSpecialty));
    this.mockMvc.perform(post("/api/specialties/")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testUpdateSpecialtySuccess() throws Exception {
    given(this.clinicService.findSpecialtyById(2)).willReturn(specialties.get(1));
    Specialty newSpecialty = specialties.get(1);
    newSpecialty.setName("surgery I");
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(specialtyMapper.toSpecialtyDto(newSpecialty));
    this.mockMvc.perform(put("/api/specialties/2")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().contentType("application/json"))
        .andExpect(status().isNoContent());

    this.mockMvc.perform(get("/api/specialties/2")
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("surgery I"));
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testUpdateSpecialtyError() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    newSpecialty.setName("");
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(specialtyMapper.toSpecialtyDto(newSpecialty));
    this.mockMvc.perform(put("/api/specialties/1")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testDeleteSpecialtySuccess() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(specialtyMapper.toSpecialtyDto(newSpecialty));
    given(this.clinicService.findSpecialtyById(1)).willReturn(specialties.get(0));
    this.mockMvc.perform(delete("/api/specialties/1")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles="VET_ADMIN")
  void testDeleteSpecialtyError() throws Exception {
    Specialty newSpecialty = specialties.get(0);
    ObjectMapper mapper = new ObjectMapper();
    String newSpecialtyAsJSON = mapper.writeValueAsString(specialtyMapper.toSpecialtyDto(newSpecialty));
    given(this.clinicService.findSpecialtyById(999)).willReturn(null);
    this.mockMvc.perform(delete("/api/specialties/999")
            .content(newSpecialtyAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }
}
