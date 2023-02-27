package com.example.petclinic.config;

import io.netty.handler.codec.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Component
public class CorsFilter {

  @Bean
  CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.addAllowedOrigin("*");
    corsConfig.setMaxAge(8000L);
    corsConfig.addAllowedMethod(HttpMethod.GET.name());
    corsConfig.addAllowedMethod(HttpMethod.POST.name());
    corsConfig.addAllowedMethod(HttpMethod.PUT.name());
    corsConfig.addAllowedMethod(HttpMethod.OPTIONS.name());
    corsConfig.addAllowedHeader("Content-Type");

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    return new CorsWebFilter(source);
  }
}
