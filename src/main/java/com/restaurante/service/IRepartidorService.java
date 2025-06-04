package com.restaurante.service;
import com.restaurante.dto.RepartidorDTO;
import com.restaurante.model.Repartidor;

import java.util.List;

public interface IRepartidorService {
    RepartidorDTO crearRepartidor(RepartidorDTO dto);
    RepartidorDTO actualizarRepartidor(Long id, RepartidorDTO dto);
    List<RepartidorDTO> listarTodosRepartidores();
    RepartidorDTO obtenerRepartidorPorId(Long id);
    void eliminarRepartidor(Long id);

    Repartidor obtenerPorIdConBloqueo(Long id);
}
