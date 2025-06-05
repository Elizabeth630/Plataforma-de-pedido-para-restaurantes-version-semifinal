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

import com.restaurante.dto.PersonalCocinaDTO;
import com.restaurante.model.PersonalCocina;
import com.restaurante.repository.PersonalCocinaRepository;
import com.restaurante.service.impl.PersonalCocinaServiceImpl;

@ExtendWith(MockitoExtension.class)
class PersonalCocinaServiceTest {

    @Mock
    private PersonalCocinaRepository personalRepository;

    @InjectMocks
    private PersonalCocinaServiceImpl personalService;

    @Test
    void crearPersonal_guardaPersonal() {
        // Arrange
        PersonalCocinaDTO dto = new PersonalCocinaDTO();
        dto.setNombre("Chef Juan");
        
        PersonalCocina personalGuardado = new PersonalCocina();
        personalGuardado.setId(1L);
        
        when(personalRepository.save(any(PersonalCocina.class))).thenReturn(personalGuardado);

        // Act
        PersonalCocinaDTO resultado = personalService.crearPersonal(dto);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(personalRepository, times(1)).save(any(PersonalCocina.class));
    }

    @Test
    void actualizarPersonal_modificaPersonal() {
        // Arrange
        Long id = 1L;
        PersonalCocinaDTO dto = new PersonalCocinaDTO();
        dto.setNombre("Chef Juan Actualizado");
        
        PersonalCocina existente = new PersonalCocina();
        existente.setId(id);
        
        when(personalRepository.findById(id)).thenReturn(Optional.of(existente));
        when(personalRepository.save(any(PersonalCocina.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PersonalCocinaDTO resultado = personalService.actualizarPersonal(id, dto);

        // Assert
        assertThat(resultado.getNombre()).isEqualTo("Chef Juan Actualizado");
        verify(personalRepository, times(1)).findById(id);
        verify(personalRepository, times(1)).save(any(PersonalCocina.class));
    }

    @Test
    void listarTodoPersonal_devuelveListaDTO() {
        // Arrange
        PersonalCocina personal1 = new PersonalCocina();
        personal1.setId(1L);
        PersonalCocina personal2 = new PersonalCocina();
        personal2.setId(2L);

        when(personalRepository.findAll()).thenReturn(Arrays.asList(personal1, personal2));

        // Act
        List<PersonalCocinaDTO> resultado = personalService.listarTodoPersonal();

        // Assert
        assertThat(resultado).hasSize(2);
        verify(personalRepository, times(1)).findAll();
    }

    @Test
    void obtenerPersonalPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        PersonalCocina personal = new PersonalCocina();
        personal.setId(id);

        when(personalRepository.findById(id)).thenReturn(Optional.of(personal));

        // Act
        PersonalCocinaDTO resultado = personalService.obtenerPersonalPorId(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(personalRepository, times(1)).findById(id);
    }

    @Test
    void eliminarPersonal_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(personalRepository).deleteById(id);

        // Act
        personalService.eliminarPersonal(id);

        // Assert
        verify(personalRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelvePersonal() {
        // Arrange
        Long id = 1L;
        PersonalCocina personal = new PersonalCocina();
        personal.setId(id);
        
        when(personalRepository.findById(id)).thenReturn(Optional.of(personal));

        // Act
        PersonalCocina resultado = personalService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(personalRepository, times(1)).findById(id);
    }
}