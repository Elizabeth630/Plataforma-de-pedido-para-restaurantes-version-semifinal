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
@Table(name = "valoracion")
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    
    @Column(name = "id_pedido")
    private Long idPedido;
    
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @Column(name = "puntuacion")
    private Integer puntuacion;

    @Column(name = "comentario")
    private String comentario;
    
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Version
    private Long version;
}