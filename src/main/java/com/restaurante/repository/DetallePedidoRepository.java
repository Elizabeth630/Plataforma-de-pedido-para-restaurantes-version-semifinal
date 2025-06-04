package com.restaurante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurante.model.DetallePedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    // Cambiar findByPedidoId a findByPedidoPid
    List<DetallePedido> findByPedidoPid(Long pid);
    
    List<DetallePedido> findByIdProducto(Long idProducto);
    
    List<DetallePedido> findByInstruccionesEspecialIsNotNull();
    
    // Cambiar deleteByPedidoId a deleteByPedidoPid
    void deleteByPedidoPid(Long pid);
}