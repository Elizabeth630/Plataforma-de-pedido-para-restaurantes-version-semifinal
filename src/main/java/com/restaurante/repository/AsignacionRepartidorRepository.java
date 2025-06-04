package com.restaurante.repository;

import com.restaurante.model.AsignacionRepartidor;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AsignacionRepartidorRepository extends JpaRepository<AsignacionRepartidor, Long> {
    Optional<AsignacionRepartidor> findByIdPedido(Long idPedido);
    List<AsignacionRepartidor> findByIdRepartidor(Long idRepartidor);
    List<AsignacionRepartidor> findByFechaAsignacionBetween(LocalDateTime inicio, LocalDateTime fin);
    List<AsignacionRepartidor> findByIdRepartidorAndFechaEntregaIsNull(Long idRepartidor);
    List<AsignacionRepartidor> findByFechaEntregaIsNull();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AsignacionRepartidor> findById(Long idPedido);
}