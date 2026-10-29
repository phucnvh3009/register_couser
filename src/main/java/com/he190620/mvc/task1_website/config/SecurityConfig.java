package com.he190620.mvc.task1_website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final GoogleLoginSuccessHandler googleLoginSuccessHandler;

    public SecurityConfig(
            GoogleLoginSuccessHandler googleLoginSuccessHandler) {
        this.googleLoginSuccessHandler = googleLoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(googleLoginSuccessHandler)
                        .failureUrl("/login?googleError=true")
                );

        return http.build();
    }
}