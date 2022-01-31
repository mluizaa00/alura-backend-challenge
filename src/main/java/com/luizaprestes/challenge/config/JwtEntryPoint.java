package com.luizaprestes.challenge.config;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public final class JwtEntryPoint implements AuthenticationEntryPoint, Serializable {

  @Override
  @SneakyThrows
  public void commence(final HttpServletRequest request, final HttpServletResponse response,
      final AuthenticationException authException) {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }

}