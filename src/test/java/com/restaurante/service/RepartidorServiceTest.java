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

import com.restaurante.dto.RepartidorDTO;
import com.restaurante.model.Repartidor;
import com.restaurante.repository.RepartidorRepository;
import com.restaurante.service.impl.RepartidorServiceImpl;

@ExtendWith(MockitoExtension.class)
class RepartidorServiceTest {

    @Mock
    private RepartidorRepository repartidorRepository;

    @InjectMocks
    private RepartidorServiceImpl repartidorService;

    @Test
    void crearRepartidor_guardaRepartidor() {
        // Arrange
        RepartidorDTO dto = new RepartidorDTO();
        dto.setNombre("Repartidor Juan");
        
        Repartidor repartidorGuardado = new Repartidor();
        repartidorGuardado.setId(1L);
        
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidorGuardado);

        // Act
        RepartidorDTO resultado = repartidorService.crearRepartidor(dto);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repartidorRepository, times(1)).save(any(Repartidor.class));
    }

    @Test
    void actualizarRepartidor_modificaRepartidor() {
        // Arrange
        Long id = 1L;
        RepartidorDTO dto = new RepartidorDTO();
        dto.setNombre("Repartidor Juan Actualizado");
        
        Repartidor existente = new Repartidor();
        existente.setId(id);
        
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(existente));
        when(repartidorRepository.save(any(Repartidor.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        RepartidorDTO resultado = repartidorService.actualizarRepartidor(id, dto);

        // Assert
        assertThat(resultado.getNombre()).isEqualTo("Repartidor Juan Actualizado");
        verify(repartidorRepository, times(1)).findById(id);
        verify(repartidorRepository, times(1)).save(any(Repartidor.class));
    }

    @Test
    void listarTodosRepartidores_devuelveListaDTO() {
        // Arrange
        Repartidor repartidor1 = new Repartidor();
        repartidor1.setId(1L);
        Repartidor repartidor2 = new Repartidor();
        repartidor2.setId(2L);

        when(repartidorRepository.findAll()).thenReturn(Arrays.asList(repartidor1, repartidor2));

        // Act
        List<RepartidorDTO> resultado = repartidorService.listarTodosRepartidores();

        // Assert
        assertThat(resultado).hasSize(2);
        verify(repartidorRepository, times(1)).findAll();
    }

    @Test
    void obtenerRepartidorPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        Repartidor repartidor = new Repartidor();
        repartidor.setId(id);

        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidor));

        // Act
        RepartidorDTO resultado = repartidorService.obtenerRepartidorPorId(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(repartidorRepository, times(1)).findById(id);
    }

    @Test
    void eliminarRepartidor_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(repartidorRepository).deleteById(id);

        // Act
        repartidorService.eliminarRepartidor(id);

        // Assert
        verify(repartidorRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelveRepartidor() {
        // Arrange
        Long id = 1L;
        Repartidor repartidor = new Repartidor();
        repartidor.setId(id);
        
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidor));

        // Act
        Repartidor resultado = repartidorService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(repartidorRepository, times(1)).findById(id);
    }
}