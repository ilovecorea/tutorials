package org.example.petclinic.config;

import org.example.petclinic.projection.PetTypeProjection;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

  @Override
  public void configureRepositoryRestConfiguration(
      RepositoryRestConfiguration repositoryRestConfiguration,
      CorsRegistry cors) {
    //petType 프로젝션을 등록
    repositoryRestConfiguration.getProjectionConfiguration().addProjection(PetTypeProjection.class);
  }
}
