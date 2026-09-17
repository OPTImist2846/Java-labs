package com.example.fpvfleet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Вимикаємо CSRF для зручності тестування POST-запитів
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Тимчасово дозволяємо доступ до всіх ендпоінтів без логіну
                );

        return http.build();
    }
}