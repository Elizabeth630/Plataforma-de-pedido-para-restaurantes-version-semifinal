package com.restaurante.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductoDTO implements Serializable {
    private Long id;
    @NotNull(message = "ID de categoría es obligatorio")
    private Long idCategoria;
    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
     @NotNull(message = "Precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    private BigDecimal precio;
    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    private String imagenUrl;
    @NotNull(message = "Tiempo de preparación es obligatorio")
    private Time tiempoPreparacion;
    @Size(max = 1000, message = "Los ingredientes no pueden exceder 1000 caracteres")
    private String ingredientes;
    @NotBlank(message = "Estado es obligatorio")
    @Pattern(regexp = "activo|inactivo", message = "Estado debe ser activo o inactivo")
    private String estado;
    @NotNull(message = "Destacado es obligatorio")
    private Boolean destacado;
}