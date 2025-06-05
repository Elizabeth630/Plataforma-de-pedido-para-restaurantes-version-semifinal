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

import com.restaurante.dto.HistorialEstadosDTO;
import com.restaurante.model.HistorialEstados;
import com.restaurante.repository.HistorialEstadosRepository;
import com.restaurante.service.impl.HistorialEstadosServiceImpl;

@ExtendWith(MockitoExtension.class)
class HistorialEstadosServiceTest {

    @Mock
    private HistorialEstadosRepository historialRepository;

    @InjectMocks
    private HistorialEstadosServiceImpl historialService;

    @Test
    void obtenerTodos_devuelveListaDTO() {
        // Arrange
        HistorialEstados hist1 = new HistorialEstados();
        hist1.setId(1L);
        HistorialEstados hist2 = new HistorialEstados();
        hist2.setId(2L);

        when(historialRepository.findAll()).thenReturn(Arrays.asList(hist1, hist2));

        // Act
        List<HistorialEstadosDTO> resultado = historialService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(2);
        verify(historialRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        HistorialEstados historial = new HistorialEstados();
        historial.setId(id);

        when(historialRepository.findById(id)).thenReturn(Optional.of(historial));

        // Act
        HistorialEstadosDTO resultado = historialService.obtenerPorId(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(historialRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorPedido_devuelveListaDTO() {
        // Arrange
        Long idPedido = 1L;
        HistorialEstados historial = new HistorialEstados();
        historial.setIdPedido(idPedido);

        when(historialRepository.findByIdPedidoOrderByFechaCambioDesc(idPedido)).thenReturn(Arrays.asList(historial));

        // Act
        List<HistorialEstadosDTO> resultado = historialService.obtenerPorPedido(idPedido);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(historialRepository, times(1)).findByIdPedidoOrderByFechaCambioDesc(idPedido);
    }

    @Test
    void obtenerPorEstado_devuelveListaDTO() {
        // Arrange
        String estado = "PENDIENTE";
        HistorialEstados historial = new HistorialEstados();
        historial.setEstado(estado);

        when(historialRepository.findByEstado(estado)).thenReturn(Arrays.asList(historial));

        // Act
        List<HistorialEstadosDTO> resultado = historialService.obtenerPorEstado(estado);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(historialRepository, times(1)).findByEstado(estado);
    }

    @Test
    void obtenerPorCliente_devuelveListaDTO() {
        // Arrange
        Long idCliente = 1L;
        HistorialEstados historial = new HistorialEstados();
        historial.setIdCliente(idCliente);

        when(historialRepository.findByIdCliente(idCliente)).thenReturn(Arrays.asList(historial));

        // Act
        List<HistorialEstadosDTO> resultado = historialService.obtenerPorCliente(idCliente);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(historialRepository, times(1)).findByIdCliente(idCliente);
    }

    @Test
    void crear_guardaHistorial() {
        // Arrange
        HistorialEstadosDTO dto = new HistorialEstadosDTO();
        dto.setIdPedido(1L);
        
        HistorialEstados historialGuardado = new HistorialEstados();
        historialGuardado.setId(1L);
        
        when(historialRepository.save(any(HistorialEstados.class))).thenReturn(historialGuardado);

        // Act
        HistorialEstadosDTO resultado = historialService.crear(dto);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(historialRepository, times(1)).save(any(HistorialEstados.class));
    }

    @Test
    void eliminar_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(historialRepository).deleteById(id);

        // Act
        historialService.eliminar(id);

        // Assert
        verify(historialRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerUltimoEstado_devuelveDTO() {
        // Arrange
        Long idPedido = 1L;
        HistorialEstados historial = new HistorialEstados();
        historial.setIdPedido(idPedido);

        when(historialRepository.findByIdPedidoOrderByFechaCambioDesc(idPedido))
            .thenReturn(Arrays.asList(historial));

        // Act
        HistorialEstadosDTO resultado = historialService.obtenerUltimoEstado(idPedido);

        // Assert
        assertThat(resultado.getIdPedido()).isEqualTo(idPedido);
        verify(historialRepository, times(1)).findByIdPedidoOrderByFechaCambioDesc(idPedido);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelveHistorial() {
        // Arrange
        Long id = 1L;
        HistorialEstados historial = new HistorialEstados();
        historial.setId(id);
        
        when(historialRepository.findById(id)).thenReturn(Optional.of(historial));

        // Act
        HistorialEstados resultado = historialService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(historialRepository, times(1)).findById(id);
    }
}