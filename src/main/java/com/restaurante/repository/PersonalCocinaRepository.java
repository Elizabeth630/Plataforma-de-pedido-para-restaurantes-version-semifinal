package com.restaurante.repository;
//import com.restaurante.model.Pedido;
import com.restaurante.model.PersonalCocina;

import jakarta.persistence.LockModeType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PersonalCocinaRepository extends JpaRepository<PersonalCocina, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PersonalCocina> findById(Long id);
}
