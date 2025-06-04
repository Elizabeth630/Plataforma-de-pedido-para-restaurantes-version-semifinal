package com.restaurante.registro.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UsuarioController {

    @GetMapping("/public/test")
    @PreAuthorize("permitAll()")
    public String allAccess() {
        return "Contenido público";
    }

    @GetMapping("/clientes/test")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')") 
    public String clienteAccess() {
        return "Contenido para clientes";
    }

    @GetMapping("/personal-cocina/test")
    @PreAuthorize("hasRole('PERSONAL_COCINA') or hasRole('ADMIN')")
    public String personalCocinaAccess() {
        return "Contenido para personal de cocina";
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Contenido para administradores";
    }
}