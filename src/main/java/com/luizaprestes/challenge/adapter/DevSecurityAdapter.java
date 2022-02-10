package com.luizaprestes.challenge.adapter;

import com.luizaprestes.challenge.config.JwtEntryPoint;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Profile(value = {"dev", "test"})
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DevSecurityAdapter extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtEntryPoint entryPoint;

  @Override
  @SneakyThrows
  protected void configure(final HttpSecurity security) {
    security
        .authorizeRequests()
          .antMatchers("/").permitAll()
          .and()
              .exceptionHandling()
              .authenticationEntryPoint(entryPoint)
          .and()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .logout().permitAll();
  }

}
