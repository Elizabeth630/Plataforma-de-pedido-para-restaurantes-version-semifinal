package com.restaurante.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

//import com.restaurante.model.PersonalCocina;
import com.restaurante.model.Producto;

import jakarta.persistence.LockModeType;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(String estado);
    List<Producto> findByDestacado(Boolean destacado);
    List<Producto> findByCategoriaIdAndEstado(Long idCategoria, String estado);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoriaId(Long idCategoria);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Producto> findById(Long id);
}