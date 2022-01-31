package com.luizaprestes.challenge.adapter;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityAdapter extends WebSecurityConfigurerAdapter {

  @Override
  @SneakyThrows
  protected void configure(final HttpSecurity security) {
    security
        .authorizeRequests()
          .antMatchers("/").permitAll()
          .anyRequest().authenticated()
          .and()
        .logout()
          .permitAll();
  }

  @Override
  protected UserDetailsService userDetailsService() {
    final UserDetails userDetails = User.withUsername("user")
        .password("user")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(userDetails);
  }

}
