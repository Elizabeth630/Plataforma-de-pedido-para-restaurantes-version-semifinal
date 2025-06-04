package com.restaurante.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "HistorialEstados")
public class HistorialEstados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_pedido")
    private Long idPedido;
    
    private String estado;
    
    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;
    
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @Column(name = "id_personacocina")
    private Long idPersonaCocina;

    @Version
    private Long version;
}