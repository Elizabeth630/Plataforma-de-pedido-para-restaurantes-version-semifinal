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
@Table(name = "personal_cocina")
public class PersonalCocina extends Persona {
    @Column(nullable = false, length = 50)
    private String turno;
    
    @Column(nullable = false, length = 100)
    private String area;
}