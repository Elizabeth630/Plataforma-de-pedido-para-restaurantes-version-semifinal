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

import com.restaurante.dto.ValoracionDTO;
import com.restaurante.model.Valoracion;
import com.restaurante.repository.ValoracionRepository;
import com.restaurante.service.impl.ValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
class ValoracionServiceTest {

    @Mock
    private ValoracionRepository valoracionRepository;

    @InjectMocks
    private ValoracionServiceImpl valoracionService;

    @Test
    void obtenerTodas_devuelveListaDTO() {
        // Arrange
        Valoracion valoracion1 = new Valoracion();
        valoracion1.setPid(1L);
        Valoracion valoracion2 = new Valoracion();
        valoracion2.setPid(2L);

        when(valoracionRepository.findAll()).thenReturn(Arrays.asList(valoracion1, valoracion2));

        // Act
        List<ValoracionDTO> resultado = valoracionService.obtenerTodas();

        // Assert
        assertThat(resultado).hasSize(2);
        verify(valoracionRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        Valoracion valoracion = new Valoracion();
        valoracion.setPid(id);

        when(valoracionRepository.findById(id)).thenReturn(Optional.of(valoracion));

        // Act
        ValoracionDTO resultado = valoracionService.obtenerPorId(id);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(id);
        verify(valoracionRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorPedido_devuelveListaDTO() {
        // Arrange
        Long idPedido = 1L;
        Valoracion valoracion = new Valoracion();
        valoracion.setIdPedido(idPedido);

        when(valoracionRepository.findByIdPedido(idPedido)).thenReturn(Arrays.asList(valoracion));

        // Act
        List<ValoracionDTO> resultado = valoracionService.obtenerPorPedido(idPedido);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(valoracionRepository, times(1)).findByIdPedido(idPedido);
    }

    @Test
    void obtenerPorCliente_devuelveListaDTO() {
        // Arrange
        Long idCliente = 1L;
        Valoracion valoracion = new Valoracion();
        valoracion.setIdCliente(idCliente);

        when(valoracionRepository.findByIdCliente(idCliente)).thenReturn(Arrays.asList(valoracion));

        // Act
        List<ValoracionDTO> resultado = valoracionService.obtenerPorCliente(idCliente);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(valoracionRepository, times(1)).findByIdCliente(idCliente);
    }

    @Test
    void crear_guardaValoracion() {
        // Arrange
        ValoracionDTO dto = new ValoracionDTO();
        dto.setIdPedido(1L);
        
        Valoracion valoracionGuardada = new Valoracion();
        valoracionGuardada.setPid(1L);
        
        when(valoracionRepository.save(any(Valoracion.class))).thenReturn(valoracionGuardada);

        // Act
        ValoracionDTO resultado = valoracionService.crear(dto);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(1L);
        verify(valoracionRepository, times(1)).save(any(Valoracion.class));
    }

    @Test
    void eliminar_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(valoracionRepository).deleteById(id);

        // Act
        valoracionService.eliminar(id);

        // Assert
        verify(valoracionRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerPuntuacionPromedioPorPedido_devuelvePromedio() {
        // Arrange
        Long idPedido = 1L;
        Valoracion valoracion1 = new Valoracion();
        valoracion1.setPuntuacion(4);
        Valoracion valoracion2 = new Valoracion();
        valoracion2.setPuntuacion(5);

        when(valoracionRepository.findByIdPedido(idPedido))
            .thenReturn(Arrays.asList(valoracion1, valoracion2));

        // Act
        Double resultado = valoracionService.obtenerPuntuacionPromedioPorPedido(idPedido);

        // Assert
        assertThat(resultado).isEqualTo(4.5);
        verify(valoracionRepository, times(1)).findByIdPedido(idPedido);
    }

    @Test
    void obtenerPuntuacionPromedioPorCliente_devuelvePromedio() {
        // Arrange
        Long idCliente = 1L;
        Valoracion valoracion1 = new Valoracion();
        valoracion1.setPuntuacion(3);
        Valoracion valoracion2 = new Valoracion();
        valoracion2.setPuntuacion(4);

        when(valoracionRepository.findByIdCliente(idCliente))
            .thenReturn(Arrays.asList(valoracion1, valoracion2));

        // Act
        Double resultado = valoracionService.obtenerPuntuacionPromedioPorCliente(idCliente);

        // Assert
        assertThat(resultado).isEqualTo(3.5);
        verify(valoracionRepository, times(1)).findByIdCliente(idCliente);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelveValoracion() {
        // Arrange
        Long id = 1L;
        Valoracion valoracion = new Valoracion();
        valoracion.setPid(id);
        
        when(valoracionRepository.findById(id)).thenReturn(Optional.of(valoracion));

        // Act
        Valoracion resultado = valoracionService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(id);
        verify(valoracionRepository, times(1)).findById(id);
    }
}