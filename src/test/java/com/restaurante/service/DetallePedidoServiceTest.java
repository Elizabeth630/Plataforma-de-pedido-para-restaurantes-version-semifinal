package com.restaurante.service;

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

import com.restaurante.dto.DetallePedidoDTO;
import com.restaurante.model.DetallePedido;
import com.restaurante.model.Pedido;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.service.impl.DetallePedidoServiceImpl;

@ExtendWith(MockitoExtension.class)
class DetallePedidoServiceTest {

    @Mock
    private DetallePedidoRepository detalleRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private DetallePedidoServiceImpl detalleService;

    private Pedido crearPedidoMock(Long id) {
        Pedido pedido = new Pedido();
        pedido.setPid(id);
        return pedido;
    }

    private DetallePedido crearDetalleMock(Long id, Long idPedido) {
        DetallePedido detalle = new DetallePedido();
        detalle.setId(id);
        detalle.setPedido(crearPedidoMock(idPedido));
        return detalle;
    }

    @Test
    void obtenerTodos_devuelveListaDTO() {
        // Arrange
        DetallePedido detalle1 = crearDetalleMock(1L, 1L);
        DetallePedido detalle2 = crearDetalleMock(2L, 1L);

        when(detalleRepository.findAll()).thenReturn(Arrays.asList(detalle1, detalle2));

        // Act
        List<DetallePedidoDTO> resultado = detalleService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdPedido()).isEqualTo(1L);
        verify(detalleRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        DetallePedido detalle = crearDetalleMock(id, 1L);

        when(detalleRepository.findById(id)).thenReturn(Optional.of(detalle));

        // Act
        DetallePedidoDTO resultado = detalleService.obtenerPorId(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getIdPedido()).isEqualTo(1L);
        verify(detalleRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorPedido_devuelveListaDTO() {
        // Arrange
        Long idPedido = 1L;
        DetallePedido detalle = crearDetalleMock(1L, idPedido);

        when(detalleRepository.findByPedidoPid(idPedido)).thenReturn(Arrays.asList(detalle));

        // Act
        List<DetallePedidoDTO> resultado = detalleService.obtenerPorPedido(idPedido);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdPedido()).isEqualTo(idPedido);
        verify(detalleRepository, times(1)).findByPedidoPid(idPedido);
    }

    @Test
    void obtenerPorProducto_devuelveListaDTO() {
        // Arrange
        Long idProducto = 1L;
        DetallePedido detalle = crearDetalleMock(1L, 1L);
        detalle.setIdProducto(idProducto);

        when(detalleRepository.findByIdProducto(idProducto)).thenReturn(Arrays.asList(detalle));

        // Act
        List<DetallePedidoDTO> resultado = detalleService.obtenerPorProducto(idProducto);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdProducto()).isEqualTo(idProducto);
        verify(detalleRepository, times(1)).findByIdProducto(idProducto);
    }

    @Test
    void obtenerConInstruccionesEspeciales_devuelveListaDTO() {
        // Arrange
        DetallePedido detalle = crearDetalleMock(1L, 1L);
        detalle.setInstruccionesEspecial("Sin sal");

        when(detalleRepository.findByInstruccionesEspecialIsNotNull()).thenReturn(Arrays.asList(detalle));

        // Act
        List<DetallePedidoDTO> resultado = detalleService.obtenerConInstruccionesEspeciales();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getInstruccionesEspecial()).isEqualTo("Sin sal");
        verify(detalleRepository, times(1)).findByInstruccionesEspecialIsNotNull();
    }

    @Test
    void crear_guardaDetalle() {
        // Arrange
        Long idPedido = 1L;
        DetallePedidoDTO dto = DetallePedidoDTO.builder()
                .idPedido(idPedido)
                .idProducto(1L)
                .cantidad(2)
                .precioUnitario(10.0)
                .build();
        
        Pedido pedido = crearPedidoMock(idPedido);
        DetallePedido detalleGuardado = crearDetalleMock(1L, idPedido);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));
        when(detalleRepository.save(any(DetallePedido.class))).thenReturn(detalleGuardado);

        // Act
        DetallePedidoDTO resultado = detalleService.crear(dto);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getIdPedido()).isEqualTo(idPedido);
        verify(detalleRepository, times(1)).save(any(DetallePedido.class));
    }

    @Test
    void actualizar_modificaDetalle() {
        // Arrange
        Long id = 1L;
        Long idPedido = 1L;
        DetallePedidoDTO dto = DetallePedidoDTO.builder()
                .cantidad(3)
                .precioUnitario(15.0)
                .instruccionesEspecial("Sin picante")
                .build();
        
        DetallePedido existente = crearDetalleMock(id, idPedido);
        
        when(detalleRepository.findById(id)).thenReturn(Optional.of(existente));
        when(detalleRepository.save(any(DetallePedido.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        DetallePedidoDTO resultado = detalleService.actualizar(id, dto);

        // Assert
        assertThat(resultado.getCantidad()).isEqualTo(3);
        assertThat(resultado.getPrecioUnitario()).isEqualTo(15.0);
        assertThat(resultado.getInstruccionesEspecial()).isEqualTo("Sin picante");
        verify(detalleRepository, times(1)).findById(id);
        verify(detalleRepository, times(1)).save(any(DetallePedido.class));
    }

    @Test
    void eliminar_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(detalleRepository).deleteById(id);

        // Act
        detalleService.eliminar(id);

        // Assert
        verify(detalleRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminarTodosDePedido_llamaMetodoDelete() {
        // Arrange
        Long idPedido = 1L;
        doNothing().when(detalleRepository).deleteByPedidoPid(idPedido);

        // Act
        detalleService.eliminarTodosDePedido(idPedido);

        // Assert
        verify(detalleRepository, times(1)).deleteByPedidoPid(idPedido);
    }
}