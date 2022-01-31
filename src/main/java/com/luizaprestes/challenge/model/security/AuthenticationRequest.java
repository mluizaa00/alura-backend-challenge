package com.luizaprestes.challenge.model.security;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class AuthenticationRequest implements Serializable {

  private String username;
  private String password;

}
