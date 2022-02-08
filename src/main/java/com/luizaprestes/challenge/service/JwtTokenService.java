package com.luizaprestes.challenge.service;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public final class JwtTokenService implements Serializable {

  private static final long serialVersionUID;
  public static final long JWT_TOKEN_VALIDITY;

  static {
    serialVersionUID = -2550185165626007488L;
    JWT_TOKEN_VALIDITY = 5 * 60 * 60L;
  }

  @Value("${jwt.secret}")
  private String secret;

  public String getUsernameFromToken(final String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(final String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(getAllClaimsFromToken(token));
  }

  private Claims getAllClaimsFromToken(final String token) {
    return Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
  }

  private Boolean isTokenExpired(final String token) {
    final var expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateToken(final UserDetails userDetails) {
    return doGenerateToken(new HashMap<>(), userDetails.getUsername());
  }

  private String doGenerateToken(final Map<String, Object> claims, final String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuer("Alura Backend Challenge")
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean validateToken(final String token, final UserDetails userDetails) {
    final var username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}