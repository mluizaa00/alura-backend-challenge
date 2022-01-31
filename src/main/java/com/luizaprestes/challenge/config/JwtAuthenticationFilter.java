package com.luizaprestes.challenge.config;

import com.luizaprestes.challenge.util.JwtTokenUtil;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public final class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userService;

  @Autowired
  private JwtTokenUtil tokenUtil;

  @Override
  @SneakyThrows
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain chain) {
    final String requestHeader = request.getHeader("Authorization");

    if (requestHeader == null || !requestHeader.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    final String authenticationToken = requestHeader.substring(7);
    final String username = tokenUtil.getUsernameFromToken(authenticationToken);

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    final UserDetails userDetails = userService.loadUserByUsername(username);
    if (!tokenUtil.validateToken(authenticationToken, userDetails)) {
      chain.doFilter(request, response);
      return;
    }

    final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());

    final WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
    token.setDetails(details);

    SecurityContextHolder.getContext()
        .setAuthentication(token);

    chain.doFilter(request, response);
  }

}