package com.restaurante.registro.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.restaurante.registro.model.Rol;
import com.restaurante.registro.model.Usuario;
import com.restaurante.registro.repository.RolRepository;
import com.restaurante.registro.repository.UsuarioRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        inicializarRoles();
        crearAdminPorDefecto();
    }

    private void inicializarRoles() {
        if (rolRepository.count() == 0) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre(Rol.NombreRol.ROL_ADMIN);
            rolRepository.save(rolAdmin);
            
            Rol rolCliente = new Rol();
            rolCliente.setNombre(Rol.NombreRol.ROL_CLIENTE);
            rolRepository.save(rolCliente);
            
            Rol rolPersonalCocina = new Rol();
            rolPersonalCocina.setNombre(Rol.NombreRol.ROL_PERSONAL_COCINA);
            rolRepository.save(rolPersonalCocina);
            
            System.out.println("Roles inicializados en la base de datos");
        }
    }
    
    private void crearAdminPorDefecto() {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@restaurante.com");
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setActivo(true);
            
            Set<Rol> roles = new HashSet<>();
            Rol rolAdmin = rolRepository.findByNombre(Rol.NombreRol.ROL_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(rolAdmin);
            admin.setRoles(roles);
            
            usuarioRepository.save(admin);
            
            System.out.println("Usuario administrador creado: admin / admin123");
        }
    }
}