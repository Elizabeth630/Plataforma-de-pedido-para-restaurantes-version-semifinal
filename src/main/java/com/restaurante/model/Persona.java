package com.restaurante.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @Column(nullable = false, length = 100)
    @Length(min = 2, max = 100)
    private String nombre;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(name = "fecha_registro", nullable = false)
    @PastOrPresent
    private LocalDate fechaRegistro;

    @Version
    private Long version;
}
