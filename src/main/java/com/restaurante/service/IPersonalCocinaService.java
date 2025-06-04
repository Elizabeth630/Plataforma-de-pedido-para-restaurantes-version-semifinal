package com.restaurante.service;
import com.restaurante.dto.PersonalCocinaDTO;
import com.restaurante.model.PersonalCocina;

import java.util.List;

public interface IPersonalCocinaService {
    PersonalCocinaDTO crearPersonal(PersonalCocinaDTO dto);
    PersonalCocinaDTO actualizarPersonal(Long id, PersonalCocinaDTO dto);
    List<PersonalCocinaDTO> listarTodoPersonal();
    PersonalCocinaDTO obtenerPersonalPorId(Long id);
    void eliminarPersonal(Long id);

    PersonalCocina obtenerPorIdConBloqueo(Long id);
}