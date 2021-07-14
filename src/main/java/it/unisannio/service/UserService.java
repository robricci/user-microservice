package it.unisannio.service;

import it.unisannio.Role;
import it.unisannio.controller.dto.JWTTokenDTO;
import it.unisannio.controller.dto.LoginDTO;
import it.unisannio.controller.dto.RegisterDTO;
import it.unisannio.controller.dto.SessionDTO;
import it.unisannio.jwt.TokenProvider;
import it.unisannio.model.Authority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unisannio.SecurityUtils;
import it.unisannio.model.User;
import it.unisannio.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.unisannio.SecurityUtils.resolveToken;

@Service
@Transactional
public class UserService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final TokenProvider tokenProvider;
   private final AuthenticationManagerBuilder authenticationManagerBuilder;

   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider,
                      AuthenticationManagerBuilder authenticationManagerBuilder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.tokenProvider = tokenProvider;
      this.authenticationManagerBuilder = authenticationManagerBuilder;
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthorities() {
      return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
   }

   public SessionDTO loginUser(LoginDTO loginDto) {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              loginDto.getUsername(), loginDto.getPassword());

      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      SessionDTO sessionDTO = new SessionDTO();
      sessionDTO.setJwt(tokenProvider.createToken(authentication));
      sessionDTO.setRoles(authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.toList()));

      return sessionDTO;
   }

   public SessionDTO registerUser(RegisterDTO registerDto) {
      User user = new User();
      user.setFirstname(registerDto.getFirstname());
      user.setLastname(registerDto.getLastname());
      user.setEmail(registerDto.getEmail());
      user.setUsername(registerDto.getUsername());
      user.setPassword(this.passwordEncoder.encode(registerDto.getPassword()));
      user.setActivated(true);
      user.setAuthorities(new HashSet<>(Collections.singletonList(new Authority(Role.ROLE_PASSENGER))));

      User reg_user = this.userRepository.save(user);

      return this.loginUser(new LoginDTO(reg_user.getUsername(), registerDto.getPassword()));
   }

   public SessionDTO validateToken(String token) {
      SessionDTO session = new SessionDTO();
      String jwt = resolveToken(token);
      if (tokenProvider.validateToken(jwt)) {
         Authentication authentication = tokenProvider.getAuthentication(jwt);
         session.setJwt(jwt);
         session.setRoles(authentication.getAuthorities().stream()
                 .map(GrantedAuthority::getAuthority)
                 .collect(Collectors.toList()));
      }
      return session;
   }

}
