package org.example.keycloak;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.example.keycloak.JwtAuthProperties.Issuer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  private final JwtAuthProperties properties;

  public JwtAuthConverter(JwtAuthProperties properties) {
    this.properties = properties;
  }

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    Collection<GrantedAuthority> authorities = Stream.concat(
        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
        extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
    log.info("## authorities:{}", authorities);
    return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
  }

  private String getPrincipalClaimName(Jwt jwt) {
    String issureUri = jwt.getIssuer().toString();
    Optional<Issuer> issure = properties.getAuth().values().stream()
        .filter(iss -> issureUri.equals(iss.getIssuerUri()))
        .findFirst();
    if (issure.isEmpty()) {
      return null;
    }

    String claimName = JwtClaimNames.SUB;
    if (issure.get().getPrincipalAttribute() != null) {
      claimName = issure.get().getPrincipalAttribute();
    }
    return jwt.getClaim(claimName);
  }

  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
    String issureUri = jwt.getIssuer().toString();
    Optional<Issuer> issure = properties.getAuth().values().stream()
        .filter(iss -> issureUri.equals(iss.getIssuerUri()))
        .findFirst();
    if (issure.isEmpty()) {
      return null;
    }

    Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
    Map<String, Object> resource;
    Collection<String> resourceRoles;
    if (resourceAccess == null
        || (resource = (Map<String, Object>) resourceAccess.get(issure.get().getResourceId())) == null
        || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
      return Set.of();
    }
    return resourceRoles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toSet());
  }
}
