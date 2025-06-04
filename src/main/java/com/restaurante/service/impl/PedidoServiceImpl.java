package com.restaurante.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.dto.PedidoDTO;
import com.restaurante.model.Pedido;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.service.IPedidoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(value = "pedido", key = "#id")
    public PedidoDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return convertToDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerPorCliente(Long idCliente) {
        return pedidoRepository.findByIdCliente(idCliente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "pedidos_del_dia")
    public List<PedidoDTO> obtenerPedidosDelDia() {
        return pedidoRepository.findByFechaPedido(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "pedidos_del_dia", allEntries = true)
    })
    public PedidoDTO crear(PedidoDTO pedidoDTO) {
        Pedido pedido = convertToEntity(pedidoDTO);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado("PENDIENTE");
        return convertToDTO(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "pedidos_del_dia", allEntries = true),
        @CacheEvict(value = "pedido", key = "#id")
    })
    public PedidoDTO actualizar(Long id, PedidoDTO pedidoDTO) {
        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        existente.setIdCliente(pedidoDTO.getIdCliente());
        existente.setFechaPedido(pedidoDTO.getFechaPedido());
        existente.setEstado(pedidoDTO.getEstado());
        
        return convertToDTO(pedidoRepository.save(existente));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "pedidos_del_dia", allEntries = true),
        @CacheEvict(value = "pedido", key = "#id")
    })
    public PedidoDTO actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.setEstado(nuevoEstado);
        return convertToDTO(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "pedidos_del_dia", allEntries = true),
        @CacheEvict(value = "pedido", key = "#id")
    })
    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Pedido obtenerPorIdConBloqueo(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        try { 
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return pedido;
    }

    private PedidoDTO convertToDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setPid(pedido.getPid());
        dto.setIdCliente(pedido.getIdCliente());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado());
        return dto;
    }

    private Pedido convertToEntity(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setPid(dto.getPid());
        pedido.setIdCliente(dto.getIdCliente());
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setEstado(dto.getEstado());
        return pedido;
    }
}