package com.restaurante.dto;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalCocinaDTO implements Serializable{
    private Long id;
    
    @NotBlank @Size(min = 2, max = 100)
    private String nombre;
    
    @NotBlank @Email
    private String email;
    
    @NotBlank
    @Pattern(regexp = "^[+]?\\d{7,15}$")
    private String telefono;
    
    @PastOrPresent
    private LocalDate fechaRegistro;
    
    @NotBlank
    @Pattern(regexp = "^(mañana|tarde|noche)$", message = "Turno inválido")
    private String turno;
    
    @NotBlank @Size(max = 100)
    private String area;
}
