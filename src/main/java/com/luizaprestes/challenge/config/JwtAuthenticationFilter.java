package com.luizaprestes.challenge.config;

import com.luizaprestes.challenge.service.JwtTokenService;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public final class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userService;

  private final JwtTokenService tokenUtil;
  @Override
  @SneakyThrows
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain chain) {
    final var requestHeader = request.getHeader("Authorization");

    if (requestHeader == null || !requestHeader.startsWith("Bearer ")) {
      return;
    }


    final var authenticationToken = requestHeader.substring(7);
    final var username = tokenUtil.getUsernameFromToken(authenticationToken);

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    final var userDetails = userService.loadUserByUsername(username);
    if (!tokenUtil.validateToken(authenticationToken, userDetails)) {
      chain.doFilter(request, response);
      return;
    }

    final var token = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());

    final var details = new WebAuthenticationDetailsSource().buildDetails(request);
    token.setDetails(details);

    SecurityContextHolder.getContext()
        .setAuthentication(token);

    chain.doFilter(request, response);
  }

}