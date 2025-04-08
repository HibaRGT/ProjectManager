package com.example.GestionProject.config;

import com.example.GestionProject.service.implementation.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Routes réservées aux Product Owner
                        .requestMatchers("/api/epic/**", "/api/productBacklog/**", "/api/projects/**")
                        .hasRole("PRODUCT_OWNER")

                        // Routes accessibles au Scrum Master
                        .requestMatchers("/api/sprint/**", "/api/sprintBacklog/**").hasRole("SCRUM_MASTER")

                        // Routes accessibles aux développeurs
                        .requestMatchers("/api/task/**", "/api/userStory/**")
                        .hasAnyRole("DEVELOPER", "SCRUM_MASTER")

                        // Toute autre route nécessite une auth
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
