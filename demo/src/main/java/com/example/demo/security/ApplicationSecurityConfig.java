package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.demo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //TODO: Understand what this is for
                .authorizeRequests()
                .antMatchers( "/", "index", "/css/*", "/js/*") // "/" is the root page
                .permitAll()// this makes sure we are whitelisting all the pages which is mentioned in the antmatchers
                .antMatchers("/api/**")
                .hasRole(STUDENT.name())// This will make sure all the endpoints starting with /api is accessible to only the ones with Student role
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails student = User.builder()
                .username("student1")
                .password(passwordEncoder.encode("password"))
                .roles(STUDENT.name()).build();
        UserDetails admin = User.builder()
                .username("admin1")
                .password(passwordEncoder.encode("password"))
                .roles(ADMIN.name()).build();
        UserDetails adminTrainee = User.builder()
                .username("adminTrainee1")
                .password(passwordEncoder.encode("password"))
                .roles(ADMINTRAINEE.name()).build();
        return new InMemoryUserDetailsManager(student, admin, adminTrainee);
    }
}