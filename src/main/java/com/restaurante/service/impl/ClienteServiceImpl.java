package com.restaurante.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.dto.ClienteDTO;
import com.restaurante.model.Cliente;
import com.restaurante.repository.ClienteRepository;
import com.restaurante.service.IClienteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "clientes", allEntries = true)
    })
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        Cliente cliente = convertirAEntity(clienteDTO);
        return convertirADTO(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "clientes", allEntries = true),
        @CacheEvict(value = "cliente", key = "#id")
    })
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        existente.setNombre(clienteDTO.getNombre());
        existente.setEmail(clienteDTO.getEmail());
        existente.setTelefono(clienteDTO.getTelefono());
        existente.setDireccion(clienteDTO.getDireccion());
        
        return convertirADTO(clienteRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "clientes")
    public List<ClienteDTO> listarTodosClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(value = "cliente", key = "#id")
    public ClienteDTO obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "clientes", allEntries = true),
        @CacheEvict(value = "cliente", key = "#id")
    })
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Cliente obtenerClientePorIdConBloqueo(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        try { 
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return cliente;
    }

    private Cliente convertirAEntity(ClienteDTO dto) {
        return Cliente.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .fechaRegistro(dto.getFechaRegistro())
                .direccion(dto.getDireccion())
                .build();
    }

    private ClienteDTO convertirADTO(Cliente entity) {
        return ClienteDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .fechaRegistro(entity.getFechaRegistro())
                .direccion(entity.getDireccion())
                .build();
    }
}