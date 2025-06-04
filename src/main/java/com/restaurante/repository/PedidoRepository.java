package com.restaurante.repository;

//import com.restaurante.model.HistorialEstados;
import com.restaurante.model.Pedido;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByIdCliente(Long idCliente);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByFechaPedidoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<Pedido> findByIdClienteAndEstado(Long idCliente, String estado);
    List<Pedido> findByFechaPedido(LocalDate fechaPedido);
    
    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido = CURRENT_DATE")
    List<Pedido> findPedidosDeHoy();
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Pedido> findById(Long id);
}