package com.rvera.sofka.banksimulator.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {
    
    private Long id;
    
    private LocalDateTime fecha;
    
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(Deposito|Retiro|Transferencia)$", message = "El tipo de movimiento debe ser: Deposito, Retiro o Transferencia")
    private String tipoMovimiento;
    
    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;
    
    private BigDecimal saldo;
    
    private Boolean estado;
    
    @NotNull(message = "El ID de la cuenta es obligatorio")
    private Long cuentaId;
    
    private CuentaDTO cuenta;
}
