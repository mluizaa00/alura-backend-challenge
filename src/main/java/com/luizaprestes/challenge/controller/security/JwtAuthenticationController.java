package com.luizaprestes.challenge.controller.security;

import com.luizaprestes.challenge.model.security.AuthenticationRequest;
import com.luizaprestes.challenge.model.security.AuthenticationResponse;
import com.luizaprestes.challenge.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public final class JwtAuthenticationController {

  @Autowired
  private AuthenticationManager manager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserDetailsService userService;

  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(final @RequestBody AuthenticationRequest request) {
    manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    final UserDetails userDetails = userService
        .loadUserByUsername(request.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);
    return ResponseEntity
        .ok(new AuthenticationResponse(token));
  }

}