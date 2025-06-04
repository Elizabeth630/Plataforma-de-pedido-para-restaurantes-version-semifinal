package com.restaurante.repository;

//import com.restaurante.model.DetallePedido;
import com.restaurante.model.HistorialEstados;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface HistorialEstadosRepository extends JpaRepository<HistorialEstados, Long> {
    List<HistorialEstados> findByIdPedido(Long idPedido);
    List<HistorialEstados> findByIdPedidoOrderByFechaCambioDesc(Long idPedido);
    List<HistorialEstados> findByEstado(String estado);
    List<HistorialEstados> findByIdCliente(Long idCliente);
    
    @Query("SELECT h FROM HistorialEstados h WHERE h.idPedido = ?1 AND h.estado = ?2 ORDER BY h.fechaCambio DESC LIMIT 1")
    Optional<HistorialEstados> findUltimoEstado(Long idPedido, String estado);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<HistorialEstados> findById(Long id);
}