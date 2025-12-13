package com.sisima.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sisima.api.filter.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //         .formLogin(login -> login.disable())
    //         .csrf(csrf -> csrf.disable())
            
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/").permitAll()
    //             .requestMatchers("/login").permitAll()
    //             .requestMatchers("/akun").hasAuthority("ROLE_ROOT")
    //             .anyRequest().authenticated()
    //         )

    //         .sessionManagement(session -> session
    //             .sessionCreationPolicy(
    //                 org.springframework.security.config.http.SessionCreationPolicy.STATELESS
    //             )
    //         )

    //         .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

    //         .httpBasic(httpBasic -> httpBasic.disable());

    //     return http.build();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
