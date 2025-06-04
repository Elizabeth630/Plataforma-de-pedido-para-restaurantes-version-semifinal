package com.restaurante.service;

import java.util.List;
import com.restaurante.dto.ValoracionDTO;
import com.restaurante.model.Valoracion;

public interface IValoracionService {
    List<ValoracionDTO> obtenerTodas();
    ValoracionDTO obtenerPorId(Long id);
    List<ValoracionDTO> obtenerPorPedido(Long idPedido);
    List<ValoracionDTO> obtenerPorCliente(Long idCliente);
    ValoracionDTO crear(ValoracionDTO valoracionDTO);
    ValoracionDTO actualizar(Long id, ValoracionDTO valoracionDTO);
    void eliminar(Long id);
    Double obtenerPuntuacionPromedioPorPedido(Long idPedido);
    Double obtenerPuntuacionPromedioPorCliente(Long idCliente);

    Valoracion obtenerPorIdConBloqueo(Long id);
}