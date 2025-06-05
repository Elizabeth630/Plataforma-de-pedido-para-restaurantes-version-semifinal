package com.restaurante.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.restaurante.dto.PedidoDTO;
import com.restaurante.model.Pedido;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.service.impl.PedidoServiceImpl;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void obtenerTodos_devuelveListaDTO() {
        // Arrange
        Pedido pedido1 = new Pedido();
        pedido1.setPid(1L);
        Pedido pedido2 = new Pedido();
        pedido2.setPid(2L);

        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedido1, pedido2));

        // Act
        List<PedidoDTO> resultado = pedidoService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(2);
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setPid(id);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        // Act
        PedidoDTO resultado = pedidoService.obtenerPorId(id);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(id);
        verify(pedidoRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorCliente_devuelveListaDTO() {
        // Arrange
        Long idCliente = 1L;
        Pedido pedido = new Pedido();
        pedido.setIdCliente(idCliente);

        when(pedidoRepository.findByIdCliente(idCliente)).thenReturn(Arrays.asList(pedido));

        // Act
        List<PedidoDTO> resultado = pedidoService.obtenerPorCliente(idCliente);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(pedidoRepository, times(1)).findByIdCliente(idCliente);
    }

    @Test
    void obtenerPorEstado_devuelveListaDTO() {
        // Arrange
        String estado = "PENDIENTE";
        Pedido pedido = new Pedido();
        pedido.setEstado(estado);

        when(pedidoRepository.findByEstado(estado)).thenReturn(Arrays.asList(pedido));

        // Act
        List<PedidoDTO> resultado = pedidoService.obtenerPorEstado(estado);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(pedidoRepository, times(1)).findByEstado(estado);
    }

    @Test
    void obtenerPedidosDelDia_devuelveListaDTO() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.now());

        when(pedidoRepository.findByFechaPedido(LocalDate.now())).thenReturn(Arrays.asList(pedido));

        // Act
        List<PedidoDTO> resultado = pedidoService.obtenerPedidosDelDia();

        // Assert
        assertThat(resultado).hasSize(1);
        verify(pedidoRepository, times(1)).findByFechaPedido(LocalDate.now());
    }

    @Test
    void crear_guardaPedido() {
        // Arrange
        PedidoDTO dto = new PedidoDTO();
        dto.setIdCliente(1L);
        
        Pedido pedidoGuardado = new Pedido();
        pedidoGuardado.setPid(1L);
        
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardado);

        // Act
        PedidoDTO resultado = pedidoService.crear(dto);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void actualizar_modificaPedido() {
        // Arrange
        Long id = 1L;
        PedidoDTO dto = new PedidoDTO();
        dto.setEstado("NUEVO_ESTADO");
        
        Pedido existente = new Pedido();
        existente.setPid(id);
        
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PedidoDTO resultado = pedidoService.actualizar(id, dto);

        // Assert
        assertThat(resultado.getEstado()).isEqualTo("NUEVO_ESTADO");
        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void actualizarEstado_modificaEstado() {
        // Arrange
        Long id = 1L;
        String nuevoEstado = "ENTREGADO";
        
        Pedido pedido = new Pedido();
        pedido.setPid(id);
        
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PedidoDTO resultado = pedidoService.actualizarEstado(id, nuevoEstado);

        // Assert
        assertThat(resultado.getEstado()).isEqualTo(nuevoEstado);
        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void eliminar_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(pedidoRepository).deleteById(id);

        // Act
        pedidoService.eliminar(id);

        // Assert
        verify(pedidoRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelvePedido() {
        // Arrange
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setPid(id);
        
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        // Act
        Pedido resultado = pedidoService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(id);
        verify(pedidoRepository, times(1)).findById(id);
    }
}