package com.restaurante.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurante.dto.CategoriaDTO;
import com.restaurante.model.Categoria;
import com.restaurante.repository.CategoriaRepository;
import com.restaurante.repository.ProductoRepository;
import com.restaurante.service.ICategoriaService;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categorias")
    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categorias_activas")
    public List<CategoriaDTO> obtenerActivas() {
        return categoriaRepository.findByEstado("activo").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(value = "categoria", key = "#id")
    public CategoriaDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return convertToDTO(categoria);
    }

    @Override
    @Transactional
    @Caching(
        evict = {
            @CacheEvict(value = "categorias", allEntries = true),
            @CacheEvict(value = "categorias_activas", allEntries = true),
           
        }
    )
    public CategoriaDTO crear(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertToEntity(categoriaDTO);
        categoria.setEstado("activo");
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertToDTO(categoriaGuardada);
    }

    @Override
    //@Transactional(readOnly = false) // Asegúrate de que no sea de solo lectura
    @Transactional
    @Caching(
        evict = {
            @CacheEvict(value = "categorias", allEntries = true),
            @CacheEvict(value = "categorias_activas", allEntries = true),
            @CacheEvict(value = "categoria", key = "#id")
        }
    )
    public CategoriaDTO actualizar(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        categoriaExistente.setNombre(categoriaDTO.getNombre());
        categoriaExistente.setDescripcion(categoriaDTO.getDescripcion());
        categoriaExistente.setImagenUrl(categoriaDTO.getImagenUrl());
        
        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
        return convertToDTO(categoriaActualizada);
    }

    @Override
    @Transactional
    @Caching(
        evict = {
            @CacheEvict(value = "categorias", allEntries = true),
            @CacheEvict(value = "categorias_activas", allEntries = true),
            @CacheEvict(value = "categoria", key = "#id")
        }
    )
    public CategoriaDTO desactivar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setEstado("inactivo");
        Categoria categoriaDesactivada = categoriaRepository.save(categoria);
        return convertToDTO(categoriaDesactivada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Categoria obtenerPorIdConBloqueo(Long id) {
        Categoria est = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        try { Thread.sleep(15000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return est;
    }

    @Override
    @Transactional
    @Caching(
        evict = {
            @CacheEvict(value = "categorias", allEntries = true),
            @CacheEvict(value = "categorias_activas", allEntries = true),
            @CacheEvict(value = "categoria", key = "#id")
        }
    )
    public void eliminar(Long id) {
        boolean tieneProductos = !productoRepository.findByCategoriaId(id).isEmpty();
        if (tieneProductos) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO convertToDTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .imagenUrl(categoria.getImagenUrl())
                .estado(categoria.getEstado())
                .build();
    }

    private Categoria convertToEntity(CategoriaDTO categoriaDTO) {
        return Categoria.builder()
                .id(categoriaDTO.getId())
                .nombre(categoriaDTO.getNombre())
                .descripcion(categoriaDTO.getDescripcion())
                .imagenUrl(categoriaDTO.getImagenUrl())
                .estado(categoriaDTO.getEstado())
                .build();
    }
}