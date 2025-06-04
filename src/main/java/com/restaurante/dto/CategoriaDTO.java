package com.restaurante.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO implements Serializable{
    private Long id;
    
    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    private String imagenUrl;
    
    @NotBlank(message = "Estado es obligatorio")
    @Pattern(regexp = "activo|inactivo", message = "Estado debe ser activo o inactivo")
    private String estado;
}