package com.restaurante.service;

import java.util.List;
import com.restaurante.dto.PedidoDTO;
import com.restaurante.model.Pedido;

public interface IPedidoService {
    List<PedidoDTO> obtenerTodos();
    PedidoDTO obtenerPorId(Long id);
    List<PedidoDTO> obtenerPorCliente(Long idCliente);
    List<PedidoDTO> obtenerPorEstado(String estado);
    PedidoDTO crear(PedidoDTO pedidoDTO);
    PedidoDTO actualizar(Long id, PedidoDTO pedidoDTO);
    PedidoDTO actualizarEstado(Long id, String nuevoEstado);
    void eliminar(Long id);
    List<PedidoDTO> obtenerPedidosDelDia();

    Pedido obtenerPorIdConBloqueo(Long idPedido);
}