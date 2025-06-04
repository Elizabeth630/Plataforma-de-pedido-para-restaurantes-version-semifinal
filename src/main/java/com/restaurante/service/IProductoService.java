package com.restaurante.service;

import java.util.List;

import com.restaurante.dto.ProductoDTO;
import com.restaurante.model.Producto;

public interface IProductoService {
    List<ProductoDTO> obtenerTodos();
    List<ProductoDTO> obtenerActivos();
    List<ProductoDTO> obtenerDestacados();
    List<ProductoDTO> obtenerPorCategoria(Long idCategoria);
    ProductoDTO obtenerPorId(Long id);
    ProductoDTO crear(ProductoDTO productoDTO);
    ProductoDTO actualizar(Long id, ProductoDTO productoDTO);
    ProductoDTO desactivar(Long id); // Baja lógica
    void eliminar(Long id); // Eliminación física
    List<ProductoDTO> buscarPorNombre(String nombre);

    Producto obtenerPorIdConBloqueo(Long id);
}