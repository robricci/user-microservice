package it.unisannio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unisannio.SecurityUtils;
import it.unisannio.model.User;
import it.unisannio.repository.UserRepository;

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
