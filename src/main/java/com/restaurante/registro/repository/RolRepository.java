package com.restaurante.registro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurante.registro.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(Rol.NombreRol nombre);
}