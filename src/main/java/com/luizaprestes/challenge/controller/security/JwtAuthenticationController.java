package com.luizaprestes.challenge.controller.security;

import com.luizaprestes.challenge.model.security.AuthenticationRequest;
import com.luizaprestes.challenge.model.security.AuthenticationResponse;
import com.luizaprestes.challenge.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class JwtAuthenticationController {

  @Autowired
  private AuthenticationManager manager;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  private UserDetailsService userService;

  @PostMapping(value = "/authenticate")
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(final @RequestBody AuthenticationRequest request) {
    try {
      final var authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
      manager.authenticate(authenticationToken);

      final var userDetails = userService
          .loadUserByUsername(request.getUsername());

      final var token = jwtTokenService.generateToken(userDetails);
      return ResponseEntity
          .ok(new AuthenticationResponse(token));
    } catch (AuthenticationException exception) {
      return ResponseEntity.badRequest().build();
    }
  }

}