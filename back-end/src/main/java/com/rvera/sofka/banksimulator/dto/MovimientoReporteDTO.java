package com.rvera.sofka.banksimulator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO para reporte de movimientos por fecha y cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoReporteDTO {
    
    private Date fecha;
    
    private String cliente;
    
    private Long numeroCuenta;
    
    private String tipo;
    
    private Float saldoInicial;
    
    private Boolean estado;
    
    private Float movimiento;
    
    private Float saldoDisponible;
}
