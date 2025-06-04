package com.restaurante.service;

import java.util.List;
import com.restaurante.dto.AsignacionRepartidorDTO;
import com.restaurante.model.AsignacionRepartidor;

public interface IAsignacionRepartidorService {
    List<AsignacionRepartidorDTO> obtenerTodas();
    AsignacionRepartidorDTO obtenerPorId(Long id);
    AsignacionRepartidorDTO obtenerPorPedido(Long idPedido);
    List<AsignacionRepartidorDTO> obtenerPorRepartidor(Long idRepartidor);
    List<AsignacionRepartidorDTO> obtenerAsignacionesPendientes();
    AsignacionRepartidorDTO crear(AsignacionRepartidorDTO asignacionDTO);
    AsignacionRepartidorDTO actualizar(Long id, AsignacionRepartidorDTO asignacionDTO);
    AsignacionRepartidorDTO registrarEntrega(Long idAsignacion);
    void eliminar(Long id);

    AsignacionRepartidor obtenerPorIdConBloqueo(Long idPedido);
}