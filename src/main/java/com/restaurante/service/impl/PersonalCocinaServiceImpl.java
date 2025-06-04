package com.restaurante.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.dto.PersonalCocinaDTO;
import com.restaurante.model.PersonalCocina;
import com.restaurante.repository.PersonalCocinaRepository;
import com.restaurante.service.IPersonalCocinaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonalCocinaServiceImpl implements IPersonalCocinaService {

    private final PersonalCocinaRepository personalRepository;

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "personal_cocina_lista", allEntries = true)
    })
    public PersonalCocinaDTO crearPersonal(PersonalCocinaDTO dto) {
        PersonalCocina personal = convertirAEntity(dto);
        return convertirADTO(personalRepository.save(personal));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "personal_cocina_lista", allEntries = true),
        @CacheEvict(value = "personal_cocina", key = "#id")
    })
    public PersonalCocinaDTO actualizarPersonal(Long id, PersonalCocinaDTO dto) {
        PersonalCocina existente = personalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personal no encontrado"));
        
        existente.setNombre(dto.getNombre());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setTurno(dto.getTurno());
        existente.setArea(dto.getArea());
        
        return convertirADTO(personalRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "personal_cocina_lista")
    public List<PersonalCocinaDTO> listarTodoPersonal() {
        return personalRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(value = "personal_cocina", key = "#id")
    public PersonalCocinaDTO obtenerPersonalPorId(Long id) {
        return personalRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Personal no encontrado"));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "personal_cocina_lista", allEntries = true),
        @CacheEvict(value = "personal_cocina", key = "#id")
    })
    public void eliminarPersonal(Long id) {
        personalRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PersonalCocina obtenerPorIdConBloqueo(Long id) {
        PersonalCocina personal = personalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personal no encontrado"));
        try { 
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return personal;
    }

    private PersonalCocina convertirAEntity(PersonalCocinaDTO dto) {
        return PersonalCocina.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .fechaRegistro(dto.getFechaRegistro())
                .turno(dto.getTurno())
                .area(dto.getArea())
                .build();
    }

    private PersonalCocinaDTO convertirADTO(PersonalCocina entity) {
        return PersonalCocinaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .fechaRegistro(entity.getFechaRegistro())
                .turno(entity.getTurno())
                .area(entity.getArea())
                .build();
    }
}