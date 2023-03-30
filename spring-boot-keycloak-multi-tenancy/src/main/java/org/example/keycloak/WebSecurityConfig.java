package org.example.keycloak;

import java.util.HashMap;
import java.util.Map;
import org.example.keycloak.JwtAuthProperties.Issuer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  public static final String ADMIN = "admin";
  public static final String USER = "user";

  private final JwtAuthProperties properties;

  private final JwtAuthConverter jwtAuthConverter;

  private Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

  private JwtIssuerAuthenticationManagerResolver authenticationManagerResolver =
      new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);

  public WebSecurityConfig(JwtAuthProperties properties, JwtAuthConverter jwtAuthConverter) {
    this.properties = properties;
    this.jwtAuthConverter = jwtAuthConverter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    properties.getAuth().forEach((s, issuer) -> addManager(authenticationManagers, issuer));
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/test/admin", "/test/admin/**").hasRole(ADMIN)
        .requestMatchers(HttpMethod.GET, "/test/user").hasAnyRole(ADMIN, USER)
        .anyRequest().authenticated();
    http.oauth2ResourceServer()
            .authenticationManagerResolver(this.authenticationManagerResolver);
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return http.build();
  }

  private void addManager(Map<String, AuthenticationManager> authenticationManagers, Issuer issuer) {
    JwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer.getIssuerUri());

    JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
    authenticationProvider.setJwtAuthenticationConverter(jwtAuthConverter);
    authenticationManagers.put(issuer.getIssuerUri(), authenticationProvider::authenticate);
  }

}
