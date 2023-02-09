package com.example.petclinic.rest.controller;

import com.example.petclinic.mapper.UserMapper;
import com.example.petclinic.rest.api.UsersApi;
import com.example.petclinic.rest.dto.UserDto;
import com.example.petclinic.service.UserService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api")
public class UserRestController implements UsersApi {

  private final UserService userService;
  private final UserMapper userMapper;

  public UserRestController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @Override
  public Mono<ResponseEntity<UserDto>> addUser(Mono<UserDto> userDto,
      ServerWebExchange exchange) {
    return userDto.flatMap(dto -> this.userService.saveUser(userMapper.toUser(dto)))
        .map(user -> ResponseEntity.created(URI.create("/api/users/" + user.getId())).build());
  }
}
