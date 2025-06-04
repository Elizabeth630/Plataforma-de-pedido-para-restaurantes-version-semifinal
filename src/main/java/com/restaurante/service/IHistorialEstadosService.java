package com.restaurante.service;

import java.util.List;
import com.restaurante.dto.HistorialEstadosDTO;
import com.restaurante.model.HistorialEstados;

public interface IHistorialEstadosService {
    List<HistorialEstadosDTO> obtenerTodos();
    HistorialEstadosDTO obtenerPorId(Long id);
    List<HistorialEstadosDTO> obtenerPorPedido(Long idPedido);
    List<HistorialEstadosDTO> obtenerPorEstado(String estado);
    List<HistorialEstadosDTO> obtenerPorCliente(Long idCliente);
    HistorialEstadosDTO crear(HistorialEstadosDTO historialDTO);
    void eliminar(Long id);
    HistorialEstadosDTO obtenerUltimoEstado(Long idPedido);

    HistorialEstados obtenerPorIdConBloqueo(Long id);
}