package com.example.petclinic.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.example.petclinic.model.Role;
import com.example.petclinic.model.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles({"local"})
class UserServiceTests extends BaseServiceTests {

  @Autowired
  private UserService userService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldAddUser() throws Exception {
    User user = User.builder()
        .username("username")
        .password("password")
        .isNew(true)
        .enabled(true)
        .roles(List.of(Role.builder().name("OWNER_ADMIN").build()))
        .build();

    userService.saveUser(user)
        .as(StepVerifier::create)
        .assertNext(u -> {
          assertThat(u.getUsername(), equalTo(user.getUsername()));
          assertThat(u.getPassword(), equalTo(user.getPassword()));
          assertThat(u.getRoles().parallelStream().allMatch(role -> role.getName().startsWith("ROLE_")), is(true));
          assertThat(u.getRoles().parallelStream().allMatch(role -> role.getUser() != null), is(true));
        })
        .verifyComplete();
  }

}
