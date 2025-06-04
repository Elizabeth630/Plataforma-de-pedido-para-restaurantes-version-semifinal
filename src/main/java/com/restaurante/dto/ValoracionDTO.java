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
public class ValoracionDTO {
    private Long pid;
    
    @NotNull(message = "ID del pedido es obligatorio")
    private Long idPedido;
    
    @NotNull(message = "ID del cliente es obligatorio")
    private Long idCliente;
    
    @NotNull(message = "Puntuación es obligatoria")
    @Min(1) @Max(5)
    private Integer puntuacion;
    
    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    private String comentario;
    
    private LocalDateTime fechaModificacion;
}