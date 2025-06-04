package com.restaurante.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurante.dto.DetallePedidoDTO;
import com.restaurante.model.DetallePedido;
import com.restaurante.model.Pedido;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.service.IDetallePedidoService;

import jakarta.transaction.Transactional;

@Service
public class DetallePedidoServiceImpl implements IDetallePedidoService {

    @Autowired
    private DetallePedidoRepository detalleRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<DetallePedidoDTO> obtenerTodos() {
        return detalleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DetallePedidoDTO obtenerPorId(Long id) {
        DetallePedido detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));
        return convertToDTO(detalle);
    }

    @Override
    public List<DetallePedidoDTO> obtenerPorPedido(Long idPedido) {
        return detalleRepository.findByPedidoPid(idPedido).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetallePedidoDTO> obtenerPorProducto(Long idProducto) {
        return detalleRepository.findByIdProducto(idProducto).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetallePedidoDTO> obtenerConInstruccionesEspeciales() {
        return detalleRepository.findByInstruccionesEspecialIsNotNull().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DetallePedidoDTO crear(DetallePedidoDTO detalleDTO) {
        Pedido pedido = pedidoRepository.findById(detalleDTO.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        DetallePedido detalle = convertToEntity(detalleDTO);
        detalle.setPedido(pedido);
        
        return convertToDTO(detalleRepository.save(detalle));
    }

    @Override
    @Transactional
    public DetallePedidoDTO actualizar(Long id, DetallePedidoDTO detalleDTO) {
        DetallePedido existente = detalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));
        
        existente.setIdProducto(detalleDTO.getIdProducto());
        existente.setCantidad(detalleDTO.getCantidad());
        existente.setPrecioUnitario(detalleDTO.getPrecioUnitario());
        existente.setInstruccionesEspecial(detalleDTO.getInstruccionesEspecial());
        
        return convertToDTO(detalleRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        detalleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarTodosDePedido(Long idPedido) {
        detalleRepository.deleteByPedidoPid(idPedido);
    }

    private DetallePedidoDTO convertToDTO(DetallePedido detalle) {
        return DetallePedidoDTO.builder()
                .id(detalle.getId())
                .idPedido(detalle.getPedido().getPid())
                .idProducto(detalle.getIdProducto())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .instruccionesEspecial(detalle.getInstruccionesEspecial())
                .build();
    }

    private DetallePedido convertToEntity(DetallePedidoDTO dto) {
        return DetallePedido.builder()
                .id(dto.getId())
                .idProducto(dto.getIdProducto())
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .instruccionesEspecial(dto.getInstruccionesEspecial())
                .build();
    }
}