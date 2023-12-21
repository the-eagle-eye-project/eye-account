package com.theeagleeyeproject.eyeaccount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    /**
     * Used to retrieve the principal (userId) from the Security Context.
     *
     * @return userId/principal
     */
    public static UUID getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID principal = null;
        if (authentication != null) {
            principal = (UUID) authentication.getPrincipal();
        }
        return principal;
    }
}
