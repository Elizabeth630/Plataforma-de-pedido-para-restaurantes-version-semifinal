package com.restaurante.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO implements Serializable{
    private Long pid;
    
    @NotNull(message = "ID del cliente es obligatorio")
    private Long idCliente;
    
    @NotNull(message = "Fecha del pedido es obligatoria")
    private LocalDate fechaPedido;
    
    @NotBlank(message = "Estado es obligatorio")
    @Size(max = 50)
    private String estado;
}