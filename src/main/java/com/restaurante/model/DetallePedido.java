package com.restaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "DetallePedido")
public class DetallePedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;
    
    @Column(name = "id_producto", nullable = false)
    private Long idProducto;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;
    
    @Column(name = "instrucciones_especial", length = 500)
    private String instruccionesEspecial;
}