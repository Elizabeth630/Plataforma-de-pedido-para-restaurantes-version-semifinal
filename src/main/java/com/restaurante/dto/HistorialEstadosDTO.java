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
public class HistorialEstadosDTO {
    private Long id;
    
    @NotNull(message = "ID del pedido es obligatorio")
    private Long idPedido;
    
    @NotBlank(message = "Estado es obligatorio")
    @Size(max = 50)
    private String estado;
    
    @NotNull(message = "Fecha de cambio es obligatoria")
    private LocalDateTime fechaCambio;
    
    @NotNull(message = "ID del cliente es obligatorio")
    private Long idCliente;
    
    private Long idPersonaCocina;
}
