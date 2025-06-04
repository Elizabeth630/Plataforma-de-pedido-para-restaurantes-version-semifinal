package com.restaurante.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.dto.ProductoDTO;
import com.restaurante.model.Categoria;
import com.restaurante.model.Producto;
import com.restaurante.repository.CategoriaRepository;
import com.restaurante.repository.ProductoRepository;
import com.restaurante.service.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "productos")
    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "productos_activos")
    public List<ProductoDTO> obtenerActivos() {
        return productoRepository.findByEstado("activo").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "productos_destacados")
    public List<ProductoDTO> obtenerDestacados() {
        return productoRepository.findByDestacado(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "productos_por_categoria", key = "#idCategoria")
    public List<ProductoDTO> obtenerPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoriaIdAndEstado(idCategoria, "activo").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(value = "producto", key = "#id")
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return convertToDTO(producto);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "productos", allEntries = true),
        @CacheEvict(value = "productos_activos", allEntries = true),
        @CacheEvict(value = "productos_destacados", allEntries = true),
        @CacheEvict(value = "productos_por_categoria", allEntries = true)
    })
    public ProductoDTO crear(ProductoDTO productoDTO) {
        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        Producto producto = convertToEntity(productoDTO);
        producto.setCategoria(categoria);
        producto.setEstado("activo");
        
        Producto productoGuardado = productoRepository.save(producto);
        return convertToDTO(productoGuardado);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "productos", allEntries = true),
        @CacheEvict(value = "productos_activos", allEntries = true),
        @CacheEvict(value = "productos_destacados", allEntries = true),
        @CacheEvict(value = "productos_por_categoria", allEntries = true),
        @CacheEvict(value = "producto", key = "#id")
    })
    public ProductoDTO actualizar(Long id, ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        productoExistente.setCategoria(categoria);
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setImagenUrl(productoDTO.getImagenUrl());
        productoExistente.setTiempoPreparacion(productoDTO.getTiempoPreparacion());
        productoExistente.setIngredientes(productoDTO.getIngredientes());
        productoExistente.setDestacado(productoDTO.getDestacado());
        
        Producto productoActualizado = productoRepository.save(productoExistente);
        return convertToDTO(productoActualizado);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "productos", allEntries = true),
        @CacheEvict(value = "productos_activos", allEntries = true),
        @CacheEvict(value = "productos_destacados", allEntries = true),
        @CacheEvict(value = "productos_por_categoria", allEntries = true),
        @CacheEvict(value = "producto", key = "#id")
    })
    public ProductoDTO desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setEstado("inactivo");
        Producto productoDesactivado = productoRepository.save(producto);
        return convertToDTO(productoDesactivado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Producto obtenerPorIdConBloqueo(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        try { 
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return producto;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "productos", allEntries = true),
        @CacheEvict(value = "productos_activos", allEntries = true),
        @CacheEvict(value = "productos_destacados", allEntries = true),
        @CacheEvict(value = "productos_por_categoria", allEntries = true),
        @CacheEvict(value = "producto", key = "#id")
    })
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    private ProductoDTO convertToDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .idCategoria(producto.getCategoria().getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .imagenUrl(producto.getImagenUrl())
                .tiempoPreparacion(producto.getTiempoPreparacion())
                .ingredientes(producto.getIngredientes())
                .estado(producto.getEstado())
                .destacado(producto.getDestacado())
                .build();
    }

    private Producto convertToEntity(ProductoDTO productoDTO) {
        return Producto.builder()
                .id(productoDTO.getId())
                .nombre(productoDTO.getNombre())
                .descripcion(productoDTO.getDescripcion())
                .precio(productoDTO.getPrecio())
                .imagenUrl(productoDTO.getImagenUrl())
                .tiempoPreparacion(productoDTO.getTiempoPreparacion())
                .ingredientes(productoDTO.getIngredientes())
                .estado(productoDTO.getEstado())
                .destacado(productoDTO.getDestacado())
                .build();
    }
}