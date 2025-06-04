package com.restaurante.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cliente")
public class Cliente extends Persona {
    @Column(nullable = false, length = 200)
    private String direccion;

    /*@Version
    private Long version;*/
}
