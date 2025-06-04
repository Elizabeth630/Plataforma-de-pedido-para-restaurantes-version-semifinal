package com.restaurante.repository;
import com.restaurante.model.Repartidor;

import jakarta.persistence.LockModeType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Repartidor> findById(Long id);
}
