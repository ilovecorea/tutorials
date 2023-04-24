package com.example.service;

import com.example.model.Provider;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public void processOAuthPostLogin(String username) {
    User existUser = userRepository.getUserByUsername(username);

    if (existUser == null) {
      User newUser = new User();
      newUser.setUsername(username);
      newUser.setProvider(Provider.GOOGLE);
      newUser.setEnabled(true);

      userRepository.save(newUser);

      log.info("Created new user: " + username);
    }
  }
}
