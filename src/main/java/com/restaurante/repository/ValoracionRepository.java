package com.restaurante.repository;

//import com.restaurante.model.Repartidor;
import com.restaurante.model.Valoracion;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {
    List<Valoracion> findByIdPedido(Long idPedido);
    List<Valoracion> findByIdCliente(Long idCliente);
    List<Valoracion> findByPuntuacionBetween(Integer min, Integer max);
    List<Valoracion> findByIdClienteAndPuntuacionGreaterThanEqual(Long idCliente, Integer puntuacionMinima);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Valoracion> findById(Long id);

}