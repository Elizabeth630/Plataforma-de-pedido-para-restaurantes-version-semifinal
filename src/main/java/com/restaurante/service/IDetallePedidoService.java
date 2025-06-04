package com.restaurante.service;

import java.util.List;

import com.restaurante.dto.DetallePedidoDTO;

public interface IDetallePedidoService {
    List<DetallePedidoDTO> obtenerTodos();
    DetallePedidoDTO obtenerPorId(Long id);
    List<DetallePedidoDTO> obtenerPorPedido(Long idPedido);
    List<DetallePedidoDTO> obtenerPorProducto(Long idProducto);
    List<DetallePedidoDTO> obtenerConInstruccionesEspeciales();
    DetallePedidoDTO crear(DetallePedidoDTO detalleDTO);
    DetallePedidoDTO actualizar(Long id, DetallePedidoDTO detalleDTO);
    void eliminar(Long id);
    void eliminarTodosDePedido(Long idPedido);
}