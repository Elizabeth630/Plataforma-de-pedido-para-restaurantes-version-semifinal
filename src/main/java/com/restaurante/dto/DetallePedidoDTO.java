package com.restaurante.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    private Long id;
    
    @NotNull(message = "ID del pedido es obligatorio")
    private Long idPedido;
    
    @NotNull(message = "ID del producto es obligatorio")
    private Long idProducto;
    
    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    
    @NotNull(message = "Precio unitario es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double precioUnitario;
    
    @Size(max = 500, message = "Las instrucciones no pueden exceder los 500 caracteres")
    private String instruccionesEspecial;
}