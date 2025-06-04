package com.restaurante.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "asignacion_repartidor")
public class AsignacionRepartidor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    
    @Column(name = "id_pedido")
    private Long idPedido;
    
    @Column(name = "id_repartidor")
    private Long idRepartidor;
    
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Version
    private Long version;
}