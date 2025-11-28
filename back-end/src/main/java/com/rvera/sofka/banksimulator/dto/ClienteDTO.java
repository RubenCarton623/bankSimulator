package com.rvera.sofka.banksimulator.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO plano para Cliente que incluye todos los campos de Persona
 * Facilita el uso de la API sin estructuras anidadas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    private Long clienteId;
    
    // Campos heredados de Persona
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
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    
    // Campos específicos de Cliente
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
    
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
