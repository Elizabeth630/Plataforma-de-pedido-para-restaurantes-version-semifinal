package com.restaurante.registro.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTO {
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupRequest {
        @NotBlank
        @Size(min = 3, max = 20)
        private String username;

        @NotBlank
        @Size(max = 50)
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, max = 40)
        private String password;

        private String nombre;
        
        private String apellido;
        
        private Set<String> roles;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JwtResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private Set<String> roles;

        public JwtResponse(String token, Long id, String username, String email, Set<String> roles) {
            this.token = token;
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}