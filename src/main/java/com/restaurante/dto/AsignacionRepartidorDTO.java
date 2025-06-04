package com.restaurante.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionRepartidorDTO {
    private Long pid;
    
    @NotNull(message = "ID del pedido es obligatorio")
    private Long idPedido;
    
    @NotNull(message = "ID del repartidor es obligatorio")
    private Long idRepartidor;
    
    @NotNull(message = "Fecha de asignación es obligatoria")
    private LocalDateTime fechaAsignacion;
    
    private LocalDateTime fechaEntrega;
}