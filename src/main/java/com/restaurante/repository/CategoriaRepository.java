package com.restaurante.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.restaurante.model.Categoria;

import jakarta.persistence.LockModeType;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByEstado(String estado);
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Categoria> findById(Long id);
}