package com.restaurante.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.restaurante.dto.CategoriaDTO;
import com.restaurante.model.Categoria;
import com.restaurante.repository.CategoriaRepository;
import com.restaurante.repository.ProductoRepository;
import com.restaurante.service.impl.CategoriaServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void obtenerTodas_devuelveListaDTO() {
        // Arrange
        Categoria cat1 = new Categoria();
        cat1.setId(1L);
        Categoria cat2 = new Categoria();
        cat2.setId(2L);

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        // Act
        List<CategoriaDTO> resultado = categoriaService.obtenerTodas();

        // Assert
        assertThat(resultado).hasSize(2);
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void obtenerActivas_devuelveListaDTO() {
        // Arrange
        Categoria cat = new Categoria();
        cat.setEstado("activo");

        when(categoriaRepository.findByEstado("activo")).thenReturn(Collections.singletonList(cat));

        // Act
        List<CategoriaDTO> resultado = categoriaService.obtenerActivas();

        // Assert
        assertThat(resultado).hasSize(1);
        verify(categoriaRepository, times(1)).findByEstado("activo");
    }

    @Test
    void obtenerPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        Categoria categoria = new Categoria();
        categoria.setId(id);

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        // Act
        CategoriaDTO resultado = categoriaService.obtenerPorId(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(categoriaRepository, times(1)).findById(id);
    }

    @Test
    void crear_guardaCategoria() {
        // Arrange
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Nueva");
        
        Categoria categoriaGuardada = new Categoria();
        categoriaGuardada.setId(1L);
        
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaGuardada);

        // Act
        CategoriaDTO resultado = categoriaService.crear(dto);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void actualizar_modificaCategoria() {
        // Arrange
        Long id = 1L;
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Actualizada");
        
        Categoria existente = new Categoria();
        existente.setId(id);
        
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        CategoriaDTO resultado = categoriaService.actualizar(id, dto);

        // Assert
        assertThat(resultado.getNombre()).isEqualTo("Actualizada");
        verify(categoriaRepository, times(1)).findById(id);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void desactivar_cambiaEstado() {
        // Arrange
        Long id = 1L;
        Categoria categoria = new Categoria();
        categoria.setEstado("activo");
        
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        CategoriaDTO resultado = categoriaService.desactivar(id);

        // Assert
        assertThat(resultado.getEstado()).isEqualTo("inactivo");
        verify(categoriaRepository, times(1)).findById(id);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void buscarPorNombre_devuelveListaDTO() {
        // Arrange
        Categoria cat = new Categoria();
        cat.setNombre("Buscar");

        when(categoriaRepository.findByNombreContainingIgnoreCase("Buscar")).thenReturn(Collections.singletonList(cat));

        // Act
        List<CategoriaDTO> resultado = categoriaService.buscarPorNombre("Buscar");

        // Assert
        assertThat(resultado).hasSize(1);
        verify(categoriaRepository, times(1)).findByNombreContainingIgnoreCase("Buscar");
    }

    @Test
    void eliminar_conProductos_lanzaExcepcion() {
        // Arrange
        Long id = 1L;
        when(productoRepository.findByCategoriaId(id)).thenReturn(Collections.singletonList(new com.restaurante.model.Producto()));

        // Act & Assert
        assertThatThrownBy(() -> categoriaService.eliminar(id))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No se puede eliminar");
        
        verify(productoRepository, times(1)).findByCategoriaId(id);
        verify(categoriaRepository, never()).deleteById(any());
    }

    @Test
    void eliminar_sinProductos_llamaDelete() {
        // Arrange
        Long id = 1L;
        when(productoRepository.findByCategoriaId(id)).thenReturn(Collections.emptyList());
        doNothing().when(categoriaRepository).deleteById(id);

        // Act
        categoriaService.eliminar(id);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelveCategoria() {
        // Arrange
        Long id = 1L;
        Categoria categoria = new Categoria();
        categoria.setId(id);
        
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        // Act
        Categoria resultado = categoriaService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(categoriaRepository, times(1)).findById(id);
    }
}