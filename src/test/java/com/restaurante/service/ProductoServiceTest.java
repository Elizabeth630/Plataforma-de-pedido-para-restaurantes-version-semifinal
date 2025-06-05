package com.restaurante.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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

import com.restaurante.dto.ProductoDTO;
import com.restaurante.model.Categoria;
import com.restaurante.model.Producto;
import com.restaurante.repository.CategoriaRepository;
import com.restaurante.repository.ProductoRepository;
import com.restaurante.service.impl.ProductoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Categoria crearCategoriaMock(Long id) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        return categoria;
    }

    private Producto crearProductoMock(Long id, Long idCategoria) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setCategoria(crearCategoriaMock(idCategoria));
        return producto;
    }

    @Test
    void obtenerTodos_devuelveListaDTO() {
        // Arrange
        Producto producto1 = crearProductoMock(1L, 1L);
        Producto producto2 = crearProductoMock(2L, 1L);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        // Act
        List<ProductoDTO> resultado = productoService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdCategoria()).isEqualTo(1L);
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void obtenerActivos_devuelveListaDTO() {
        // Arrange
        Producto producto = crearProductoMock(1L, 1L);
        producto.setEstado("activo");

        when(productoRepository.findByEstado("activo")).thenReturn(Collections.singletonList(producto));

        // Act
        List<ProductoDTO> resultado = productoService.obtenerActivos();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("activo");
        verify(productoRepository, times(1)).findByEstado("activo");
    }

    @Test
    void obtenerDestacados_devuelveListaDTO() {
        // Arrange
        Producto producto = crearProductoMock(1L, 1L);
        producto.setDestacado(true);

        when(productoRepository.findByDestacado(true)).thenReturn(Collections.singletonList(producto));

        // Act
        List<ProductoDTO> resultado = productoService.obtenerDestacados();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDestacado()).isTrue();
        verify(productoRepository, times(1)).findByDestacado(true);
    }

    @Test
    void obtenerPorCategoria_devuelveListaDTO() {
        // Arrange
        Long idCategoria = 1L;
        Producto producto = crearProductoMock(1L, idCategoria);
        producto.setEstado("activo");

        when(productoRepository.findByCategoriaIdAndEstado(idCategoria, "activo"))
            .thenReturn(Collections.singletonList(producto));

        // Act
        List<ProductoDTO> resultado = productoService.obtenerPorCategoria(idCategoria);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdCategoria()).isEqualTo(idCategoria);
        verify(productoRepository, times(1)).findByCategoriaIdAndEstado(idCategoria, "activo");
    }

    @Test
    void obtenerPorId_devuelveDTO() {
        // Arrange
        Long id = 1L;
        Producto producto = crearProductoMock(id, 1L);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // Act
        ProductoDTO resultado = productoService.obtenerPorId(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getIdCategoria()).isEqualTo(1L);
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void crear_guardaProducto() {
        // Arrange
        Long idCategoria = 1L;
        ProductoDTO dto = ProductoDTO.builder()
                .idCategoria(idCategoria)
                .nombre("Nuevo Producto")
                .estado("activo")
                .build();
        
        Categoria categoria = crearCategoriaMock(idCategoria);
        Producto productoGuardado = crearProductoMock(1L, idCategoria);

        when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        // Act
        ProductoDTO resultado = productoService.crear(dto);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getIdCategoria()).isEqualTo(idCategoria);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void actualizar_modificaProducto() {
        // Arrange
        Long id = 1L;
        Long idCategoria = 1L;
        ProductoDTO dto = ProductoDTO.builder()
                .nombre("Producto Actualizado")
                .idCategoria(idCategoria)
                .precio(new BigDecimal("15.0"))  // Cambiado a BigDecimal
                .build();
        
        Categoria categoria = crearCategoriaMock(idCategoria);
        Producto existente = crearProductoMock(id, idCategoria);
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ProductoDTO resultado = productoService.actualizar(id, dto);

        // Assert
        assertThat(resultado.getNombre()).isEqualTo("Producto Actualizado");
        assertThat(resultado.getPrecio()).isEqualTo(new BigDecimal("15.0"));  // Cambiado a BigDecimal
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void desactivar_cambiaEstado() {
        // Arrange
        Long id = 1L;
        Producto producto = crearProductoMock(id, 1L);
        producto.setEstado("activo");
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ProductoDTO resultado = productoService.desactivar(id);

        // Assert
        assertThat(resultado.getEstado()).isEqualTo("inactivo");
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void buscarPorNombre_devuelveListaDTO() {
        // Arrange
        String nombre = "Pizza";
        Producto producto = crearProductoMock(1L, 1L);
        producto.setNombre(nombre);

        when(productoRepository.findByNombreContainingIgnoreCase(nombre))
            .thenReturn(Collections.singletonList(producto));

        // Act
        List<ProductoDTO> resultado = productoService.buscarPorNombre(nombre);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo(nombre);
        verify(productoRepository, times(1)).findByNombreContainingIgnoreCase(nombre);
    }

    @Test
    void eliminar_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(productoRepository).deleteById(id);

        // Act
        productoService.eliminar(id);

        // Assert
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void obtenerPorIdConBloqueo_devuelveProducto() {
        // Arrange
        Long id = 1L;
        Producto producto = crearProductoMock(id, 1L);
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // Act
        Producto resultado = productoService.obtenerPorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        verify(productoRepository, times(1)).findById(id);
    }
}