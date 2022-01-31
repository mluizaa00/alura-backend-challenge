package com.luizaprestes.challenge.adapter;

import com.luizaprestes.challenge.config.JwtAuthenticationFilter;
import com.luizaprestes.challenge.config.JwtEntryPoint;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityAdapter extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtEntryPoint entryPoint;

  @Autowired
  private UserDetailsService userService;

  @Autowired
  private JwtAuthenticationFilter filter;

  @Override
  @SneakyThrows
  protected void configure(final HttpSecurity security) {
    security
        .authorizeRequests()
          .antMatchers("/", "/authenticate", "/home").permitAll()
          .antMatchers("/v1/").authenticated()
          .anyRequest().authenticated()
          .and()
              .exceptionHandling()
              .authenticationEntryPoint(entryPoint)
          .and()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .logout()
          .permitAll();

    security.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }

  @Autowired @SneakyThrows
  public void configureGlobal(final AuthenticationManagerBuilder auth) {
    auth
        .userDetailsService(userService)
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
