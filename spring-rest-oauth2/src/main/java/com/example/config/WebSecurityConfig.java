package com.example.config;

import com.example.security.CustomOAuth2User;
import com.example.security.CustomOAuth2UserService;
import com.example.security.UserDetailsServiceImpl;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

  private final UserService userService;

  private final CustomOAuth2UserService oauthUserService;

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //@formatter:off
    http.authorizeRequests()
        .antMatchers("/", "/login", "/oauth/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().permitAll()
          .loginPage("/login")
          .usernameParameter("email")
          .passwordParameter("pass")
          .defaultSuccessUrl("/list")
        .and()
          .oauth2Login()
          .loginPage("/login")
          .userInfoEndpoint()
            .userService(oauthUserService)
        .and()
          .successHandler((request,response,authentication)-> {
            System.out.println("AuthenticationSuccessHandler invoked");
            System.out.println("Authentication name: " + authentication.getName());
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            userService.processOAuthPostLogin(oauthUser.getEmail());
            response.sendRedirect("/list");
          })
        //.defaultSuccessUrl("/list")
        .and()
        .logout().logoutSuccessUrl("/").permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/403");

    return http.build();
    //@formatter:on
  }
}
