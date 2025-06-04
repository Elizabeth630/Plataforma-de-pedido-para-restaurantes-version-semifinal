package com.restaurante.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;
    
    @Column(name = "estado")
    private String estado;

    @Version
    private Long version;
}