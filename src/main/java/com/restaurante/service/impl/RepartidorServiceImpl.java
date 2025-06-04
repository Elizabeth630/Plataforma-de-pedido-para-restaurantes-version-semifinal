package com.restaurante.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.dto.RepartidorDTO;
import com.restaurante.model.Repartidor;
import com.restaurante.repository.RepartidorRepository;
import com.restaurante.service.IRepartidorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements IRepartidorService {

    private final RepartidorRepository repartidorRepository;

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "repartidores", allEntries = true)
    })
    public RepartidorDTO crearRepartidor(RepartidorDTO dto) {
        Repartidor repartidor = convertirAEntity(dto);
        return convertirADTO(repartidorRepository.save(repartidor));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "repartidores", allEntries = true),
        @CacheEvict(value = "repartidor", key = "#id")
    })
    public RepartidorDTO actualizarRepartidor(Long id, RepartidorDTO dto) {
        Repartidor existente = repartidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
        
        existente.setNombre(dto.getNombre());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setZona(dto.getZona());
        
        return convertirADTO(repartidorRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "repartidores")
    public List<RepartidorDTO> listarTodosRepartidores() {
        return repartidorRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(value = "repartidor", key = "#id")
    public RepartidorDTO obtenerRepartidorPorId(Long id) {
        return repartidorRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "repartidores", allEntries = true),
        @CacheEvict(value = "repartidor", key = "#id")
    })
    public void eliminarRepartidor(Long id) {
        repartidorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Repartidor obtenerPorIdConBloqueo(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
        try { 
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return repartidor;
    }

    private Repartidor convertirAEntity(RepartidorDTO dto) {
        return Repartidor.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .fechaRegistro(dto.getFechaRegistro())
                .zona(dto.getZona())
                .build();
    }

    private RepartidorDTO convertirADTO(Repartidor entity) {
        return RepartidorDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .fechaRegistro(entity.getFechaRegistro())
                .zona(entity.getZona())
                .build();
    }
}