package com.rvera.sofka.banksimulator.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    
    private Long id;
    
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "El número de cuenta debe contener solo números (6-20 dígitos)")
    private String numeroCuenta;
    
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Pattern(regexp = "^(Ahorros|Corriente)$", message = "El tipo de cuenta debe ser: Ahorros o Corriente")
    private String tipoCuenta;
    
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal saldoInicial;
    
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
        
    private ClienteDTO cliente;
}
