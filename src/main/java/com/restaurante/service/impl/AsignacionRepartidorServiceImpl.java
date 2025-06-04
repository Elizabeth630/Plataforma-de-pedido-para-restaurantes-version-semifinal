package com.restaurante.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurante.dto.AsignacionRepartidorDTO;
import com.restaurante.model.AsignacionRepartidor;
import com.restaurante.repository.AsignacionRepartidorRepository;
import com.restaurante.service.IAsignacionRepartidorService;

import jakarta.transaction.Transactional;

@Service
public class AsignacionRepartidorServiceImpl implements IAsignacionRepartidorService {

    @Autowired
    private AsignacionRepartidorRepository asignacionRepository;

    @Override
    public List<AsignacionRepartidorDTO> obtenerTodas() {
        return asignacionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AsignacionRepartidorDTO obtenerPorId(Long id) {
        AsignacionRepartidor asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        return convertToDTO(asignacion);
    }

    @Override
    public AsignacionRepartidorDTO obtenerPorPedido(Long idPedido) {
        Optional<AsignacionRepartidor> asignacion = asignacionRepository.findByIdPedido(idPedido);
        return asignacion.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada para el pedido"));
    }

    @Override
    public List<AsignacionRepartidorDTO> obtenerPorRepartidor(Long idRepartidor) {
        return asignacionRepository.findByIdRepartidor(idRepartidor).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AsignacionRepartidorDTO> obtenerAsignacionesPendientes() {
        return asignacionRepository.findByFechaEntregaIsNull().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AsignacionRepartidorDTO crear(AsignacionRepartidorDTO asignacionDTO) {
        AsignacionRepartidor asignacion = convertToEntity(asignacionDTO);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        return convertToDTO(asignacionRepository.save(asignacion));
    }

    @Override
    public AsignacionRepartidorDTO actualizar(Long id, AsignacionRepartidorDTO asignacionDTO) {
        AsignacionRepartidor existente = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        
        existente.setIdPedido(asignacionDTO.getIdPedido());
        existente.setIdRepartidor(asignacionDTO.getIdRepartidor());
        existente.setFechaAsignacion(asignacionDTO.getFechaAsignacion());
        existente.setFechaEntrega(asignacionDTO.getFechaEntrega());
        
        return convertToDTO(asignacionRepository.save(existente));
    }

    @Override
    public AsignacionRepartidorDTO registrarEntrega(Long idAsignacion) {
        AsignacionRepartidor asignacion = asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        
        asignacion.setFechaEntrega(LocalDateTime.now());
        return convertToDTO(asignacionRepository.save(asignacion));
    }

    @Override
    public void eliminar(Long id) {
        asignacionRepository.deleteById(id);
    }

    @Transactional
    public AsignacionRepartidor obtenerPorIdConBloqueo(Long id) {
        AsignacionRepartidor est = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignacion no encontrado"));
        try { Thread.sleep(15000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return est;
    }

    private AsignacionRepartidorDTO convertToDTO(AsignacionRepartidor asignacion) {
        AsignacionRepartidorDTO dto = new AsignacionRepartidorDTO();
        dto.setPid(asignacion.getPid());
        dto.setIdPedido(asignacion.getIdPedido());
        dto.setIdRepartidor(asignacion.getIdRepartidor());
        dto.setFechaAsignacion(asignacion.getFechaAsignacion());
        dto.setFechaEntrega(asignacion.getFechaEntrega());
        return dto;
    }

    private AsignacionRepartidor convertToEntity(AsignacionRepartidorDTO dto) {
        AsignacionRepartidor asignacion = new AsignacionRepartidor();
        asignacion.setPid(dto.getPid());
        asignacion.setIdPedido(dto.getIdPedido());
        asignacion.setIdRepartidor(dto.getIdRepartidor());
        asignacion.setFechaAsignacion(dto.getFechaAsignacion());
        asignacion.setFechaEntrega(dto.getFechaEntrega());
        return asignacion;
    }
}