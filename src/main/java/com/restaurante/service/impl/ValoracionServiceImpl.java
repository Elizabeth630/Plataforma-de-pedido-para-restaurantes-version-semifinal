package com.restaurante.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.dto.ValoracionDTO;
//import com.restaurante.model.Repartidor;
import com.restaurante.model.Valoracion;
import com.restaurante.repository.ValoracionRepository;
import com.restaurante.service.IValoracionService;

@Service
public class ValoracionServiceImpl implements IValoracionService {

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Override
    public List<ValoracionDTO> obtenerTodas() {
        return valoracionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ValoracionDTO obtenerPorId(Long id) {
        Valoracion valoracion = valoracionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));
        return convertToDTO(valoracion);
    }

    @Override
    public List<ValoracionDTO> obtenerPorPedido(Long idPedido) {
        return valoracionRepository.findByIdPedido(idPedido).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ValoracionDTO> obtenerPorCliente(Long idCliente) {
        return valoracionRepository.findByIdCliente(idCliente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ValoracionDTO crear(ValoracionDTO valoracionDTO) {
        Valoracion valoracion = convertToEntity(valoracionDTO);
        valoracion.setFechaModificacion(java.time.LocalDateTime.now());
        return convertToDTO(valoracionRepository.save(valoracion));
    }

    @Override
    public ValoracionDTO actualizar(Long id, ValoracionDTO valoracionDTO) {
        Valoracion existente = valoracionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));
        
        existente.setIdPedido(valoracionDTO.getIdPedido());
        existente.setIdCliente(valoracionDTO.getIdCliente());
        existente.setPuntuacion(valoracionDTO.getPuntuacion());
        existente.setComentario(valoracionDTO.getComentario());
        existente.setFechaModificacion(java.time.LocalDateTime.now());
        
        return convertToDTO(valoracionRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        valoracionRepository.deleteById(id);
    }

    @Override
    public Double obtenerPuntuacionPromedioPorPedido(Long idPedido) {
        return valoracionRepository.findByIdPedido(idPedido).stream()
                .mapToInt(Valoracion::getPuntuacion)
                .average()
                .orElse(0.0);
    }

    @Override
    public Double obtenerPuntuacionPromedioPorCliente(Long idCliente) {
        return valoracionRepository.findByIdCliente(idCliente).stream()
                .mapToInt(Valoracion::getPuntuacion)
                .average()
                .orElse(0.0);
    }

    @Transactional
    public Valoracion obtenerPorIdConBloqueo(Long id) {
        Valoracion est = valoracionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));
        try { Thread.sleep(15000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return est;
    }

    private ValoracionDTO convertToDTO(Valoracion valoracion) {
        ValoracionDTO dto = new ValoracionDTO();
        dto.setPid(valoracion.getPid());
        dto.setIdPedido(valoracion.getIdPedido());
        dto.setIdCliente(valoracion.getIdCliente());
        dto.setPuntuacion(valoracion.getPuntuacion());
        dto.setComentario(valoracion.getComentario());
        dto.setFechaModificacion(valoracion.getFechaModificacion());
        return dto;
    }

    private Valoracion convertToEntity(ValoracionDTO dto) {
        Valoracion valoracion = new Valoracion();
        valoracion.setPid(dto.getPid());
        valoracion.setIdPedido(dto.getIdPedido());
        valoracion.setIdCliente(dto.getIdCliente());
        valoracion.setPuntuacion(dto.getPuntuacion());
        valoracion.setComentario(dto.getComentario());
        valoracion.setFechaModificacion(dto.getFechaModificacion());
        return valoracion;
    }
}