package it.unisannio.security.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unisannio.security.SecurityUtils;
import it.unisannio.security.model.User;
import it.unisannio.security.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
public class UserService {

   private final UserRepository userRepository;

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthorities() {
      return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
   }

}
