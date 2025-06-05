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

import com.restaurante.dto.AsignacionRepartidorDTO;
import com.restaurante.model.AsignacionRepartidor;
import com.restaurante.repository.AsignacionRepartidorRepository;
import com.restaurante.service.impl.AsignacionRepartidorServiceImpl;

@ExtendWith(MockitoExtension.class)
class AsignacionRepartidorServiceTest {

    @Mock
    private AsignacionRepartidorRepository asignacionRepository;

    @InjectMocks
    private AsignacionRepartidorServiceImpl asignacionService;

    @Test
    void obtenerTodas_devuelveListaDTO() {
        // Arrange
        AsignacionRepartidor asignacion1 = new AsignacionRepartidor();
        asignacion1.setPid(1L);
        AsignacionRepartidor asignacion2 = new AsignacionRepartidor();
        asignacion2.setPid(2L);

        when(asignacionRepository.findAll()).thenReturn(Arrays.asList(asignacion1, asignacion2));

        // Act
        List<AsignacionRepartidorDTO> resultado = asignacionService.obtenerTodas();

        // Assert
        assertThat(resultado).hasSize(2);
        verify(asignacionRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        AsignacionRepartidor asignacion = new AsignacionRepartidor();
        asignacion.setPid(id);

        when(asignacionRepository.findById(id)).thenReturn(Optional.of(asignacion));

        // Act
        AsignacionRepartidorDTO resultado = asignacionService.obtenerPorId(id);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(id);
        verify(asignacionRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorPedido_devuelveDTO() {
        // Arrange
        Long idPedido = 1L;
        AsignacionRepartidor asignacion = new AsignacionRepartidor();
        asignacion.setIdPedido(idPedido);

        when(asignacionRepository.findByIdPedido(idPedido)).thenReturn(Optional.of(asignacion));

        // Act
        AsignacionRepartidorDTO resultado = asignacionService.obtenerPorPedido(idPedido);

        // Assert
        assertThat(resultado.getIdPedido()).isEqualTo(idPedido);
        verify(asignacionRepository, times(1)).findByIdPedido(idPedido);
    }

    @Test
    void obtenerPorRepartidor_devuelveListaDTO() {
        // Arrange
        Long idRepartidor = 1L;
        AsignacionRepartidor asignacion = new AsignacionRepartidor();
        asignacion.setIdRepartidor(idRepartidor);

        when(asignacionRepository.findByIdRepartidor(idRepartidor)).thenReturn(Arrays.asList(asignacion));

        // Act
        List<AsignacionRepartidorDTO> resultado = asignacionService.obtenerPorRepartidor(idRepartidor);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(asignacionRepository, times(1)).findByIdRepartidor(idRepartidor);
    }

    @Test
    void obtenerAsignacionesPendientes_devuelveListaDTO() {
        // Arrange
        AsignacionRepartidor asignacion = new AsignacionRepartidor();
        asignacion.setFechaEntrega(null);

        when(asignacionRepository.findByFechaEntregaIsNull()).thenReturn(Arrays.asList(asignacion));

        // Act
        List<AsignacionRepartidorDTO> resultado = asignacionService.obtenerAsignacionesPendientes();

        // Assert
        assertThat(resultado).hasSize(1);
        verify(asignacionRepository, times(1)).findByFechaEntregaIsNull();
    }

    @Test
    void crear_guardaAsignacion() {
        // Arrange
        AsignacionRepartidorDTO dto = new AsignacionRepartidorDTO();
        dto.setIdPedido(1L);
        
        AsignacionRepartidor asignacionGuardada = new AsignacionRepartidor();
        asignacionGuardada.setPid(1L);
        
        when(asignacionRepository.save(any(AsignacionRepartidor.class))).thenReturn(asignacionGuardada);

        // Act
        AsignacionRepartidorDTO resultado = asignacionService.crear(dto);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(1L);
        verify(asignacionRepository, times(1)).save(any(AsignacionRepartidor.class));
    }

    @Test
    void actualizar_modificaAsignacion() {
        // Arrange
        Long id = 1L;
        AsignacionRepartidorDTO dto = new AsignacionRepartidorDTO();
        dto.setIdPedido(2L);
        
        AsignacionRepartidor existente = new AsignacionRepartidor();
        existente.setPid(id);
        
        when(asignacionRepository.findById(id)).thenReturn(Optional.of(existente));
        when(asignacionRepository.save(any(AsignacionRepartidor.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        AsignacionRepartidorDTO resultado = asignacionService.actualizar(id, dto);

        // Assert
        assertThat(resultado.getIdPedido()).isEqualTo(2L);
        verify(asignacionRepository, times(1)).findById(id);
        verify(asignacionRepository, times(1)).save(any(AsignacionRepartidor.class));
    }

    @Test
    void registrarEntrega_actualizaFecha() {
        // Arrange
        Long id = 1L;
        AsignacionRepartidor asignacion = new AsignacionRepartidor();
        asignacion.setPid(id);
        
        when(asignacionRepository.findById(id)).thenReturn(Optional.of(asignacion));
        when(asignacionRepository.save(any(AsignacionRepartidor.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        AsignacionRepartidorDTO resultado = asignacionService.registrarEntrega(id);

        // Assert
        assertThat(resultado.getFechaEntrega()).isNotNull();
        verify(asignacionRepository, times(1)).findById(id);
        verify(asignacionRepository, times(1)).save(any(AsignacionRepartidor.class));
    }

    @Test
    void eliminar_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(asignacionRepository).deleteById(id);

        // Act
        asignacionService.eliminar(id);

        // Assert
        verify(asignacionRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelveAsignacion() {
        // Arrange
        Long id = 1L;
        AsignacionRepartidor asignacion = new AsignacionRepartidor();
        asignacion.setPid(id);
        
        when(asignacionRepository.findById(id)).thenReturn(Optional.of(asignacion));

        // Act
        AsignacionRepartidor resultado = asignacionService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getPid()).isEqualTo(id);
        verify(asignacionRepository, times(1)).findById(id);
    }
}