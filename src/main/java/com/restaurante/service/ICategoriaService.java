package com.restaurante.service;

import java.util.List;

import com.restaurante.dto.CategoriaDTO;
import com.restaurante.model.Categoria;

public interface ICategoriaService {
    List<CategoriaDTO> obtenerTodas();
    List<CategoriaDTO> obtenerActivas();
    CategoriaDTO obtenerPorId(Long id);
    CategoriaDTO crear(CategoriaDTO categoriaDTO);
    CategoriaDTO actualizar(Long id, CategoriaDTO categoriaDTO);
    CategoriaDTO desactivar(Long id); // Baja lógica
    void eliminar(Long id); // Eliminación física
    List<CategoriaDTO> buscarPorNombre(String nombre);

    Categoria obtenerPorIdConBloqueo(Long id);
}