package com.restaurante.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurante.dto.HistorialEstadosDTO;
//import com.restaurante.model.DetallePedido;
import com.restaurante.model.HistorialEstados;
import com.restaurante.repository.HistorialEstadosRepository;
import com.restaurante.service.IHistorialEstadosService;

import jakarta.transaction.Transactional;

@Service
public class HistorialEstadosServiceImpl implements IHistorialEstadosService {

    @Autowired
    private HistorialEstadosRepository historialRepository;

    @Override
    public List<HistorialEstadosDTO> obtenerTodos() {
        return historialRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HistorialEstadosDTO obtenerPorId(Long id) {
        HistorialEstados historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de historial no encontrado"));
        return convertToDTO(historial);
    }

    @Override
    public List<HistorialEstadosDTO> obtenerPorPedido(Long idPedido) {
        return historialRepository.findByIdPedidoOrderByFechaCambioDesc(idPedido).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialEstadosDTO> obtenerPorEstado(String estado) {
        return historialRepository.findByEstado(estado).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialEstadosDTO> obtenerPorCliente(Long idCliente) {
        return historialRepository.findByIdCliente(idCliente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HistorialEstadosDTO crear(HistorialEstadosDTO historialDTO) {
        HistorialEstados historial = convertToEntity(historialDTO);
        historial.setFechaCambio(LocalDateTime.now());
        return convertToDTO(historialRepository.save(historial));
    }

    @Override
    public void eliminar(Long id) {
        historialRepository.deleteById(id);
    }

    @Override
    public HistorialEstadosDTO obtenerUltimoEstado(Long idPedido) {
        List<HistorialEstados> historial = historialRepository.findByIdPedidoOrderByFechaCambioDesc(idPedido);
        if (historial.isEmpty()) {
            throw new RuntimeException("No se encontró historial para el pedido");
        }
        return convertToDTO(historial.get(0));
    }

    @Transactional
    public HistorialEstados obtenerPorIdConBloqueo(Long id) {
        HistorialEstados est = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial de pedido no encontrado"));
        try { Thread.sleep(15000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return est;
    }

    private HistorialEstadosDTO convertToDTO(HistorialEstados historial) {
        HistorialEstadosDTO dto = new HistorialEstadosDTO();
        dto.setId(historial.getId());
        dto.setIdPedido(historial.getIdPedido());
        dto.setEstado(historial.getEstado());
        dto.setFechaCambio(historial.getFechaCambio());
        dto.setIdCliente(historial.getIdCliente());
        dto.setIdPersonaCocina(historial.getIdPersonaCocina());
        return dto;
    }

    private HistorialEstados convertToEntity(HistorialEstadosDTO dto) {
        HistorialEstados historial = new HistorialEstados();
        historial.setId(dto.getId());
        historial.setIdPedido(dto.getIdPedido());
        historial.setEstado(dto.getEstado());
        historial.setFechaCambio(dto.getFechaCambio());
        historial.setIdCliente(dto.getIdCliente());
        historial.setIdPersonaCocina(dto.getIdPersonaCocina());
        return historial;
    }
}