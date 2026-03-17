package com.example.employeemanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // disable CSRF for API testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/employees/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/employees/**").permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
