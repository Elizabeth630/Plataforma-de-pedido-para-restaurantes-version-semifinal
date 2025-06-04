package com.restaurante.repository;
//import com.restaurante.model.Categoria;
import com.restaurante.model.Cliente;

import jakarta.persistence.LockModeType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Cliente> findById(Long id);
    }
