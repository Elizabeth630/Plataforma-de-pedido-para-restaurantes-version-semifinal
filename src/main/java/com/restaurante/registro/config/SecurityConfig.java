package com.restaurante.registro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.restaurante.registro.security.JwtAuthenticationEntryPoint;
import com.restaurante.registro.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableJdbcHttpSession
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/personal-cocina/**").hasAnyRole("ADMIN", "PERSONAL_COCINA")
                .requestMatchers("/api/clientes/**").hasAnyRole("ADMIN", "CLIENTE")
                .requestMatchers("/api/pedidos/**").hasAnyRole("ADMIN", "CLIENTE", "PERSONAL_COCINA")
                .requestMatchers("/api/productos/**").hasAnyRole("ADMIN", "CLIENTE", "PERSONAL_COCINA")
                .requestMatchers("/api/categorias/**").hasAnyRole("ADMIN", "CLIENTE", "PERSONAL_COCINA")
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}