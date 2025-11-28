package com.rvera.sofka.banksimulator.service;

import java.time.LocalDateTime;

/**
 * Interface del servicio de reportes PDF
 */
public interface IReporteService {
    /**
     * Genera un reporte PDF de movimientos por cliente y rango de fechas
     * @param clienteId ID del cliente
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Byte array con el contenido del PDF
     */
    byte[] generarReporteMovimientos(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
