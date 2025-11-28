package com.rvera.sofka.banksimulator.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombre;
    
    @Pattern(regexp = "^(M|F|Masculino|Femenino|Otro)$", message = "El género debe ser: M, F, Masculino, Femenino u Otro")
    private String genero;
    
    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 120, message = "La edad máxima es 120 años")
    private Integer edad;
    
    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La identificación debe contener solo números (6-20 dígitos)")
    private String identificacion;
    
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String direccion;
    
    @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe contener solo números (7-15 dígitos)")
    private String telefono;
}
