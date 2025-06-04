package com.restaurante.dto;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO implements Serializable{
    private Long id;
    
    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;
    
    @NotBlank @Email
    private String email;
    
    @NotBlank
    @Pattern(regexp = "^[+]?\\d{7,15}$", message = "Teléfono inválido")
    private String telefono;
    
    @PastOrPresent
    private LocalDate fechaRegistro;
    
    @NotBlank
    @Size(max = 200)
    private String direccion;
}
