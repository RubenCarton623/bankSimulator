package com.rvera.sofka.banksimulator.service;

import com.rvera.sofka.banksimulator.dto.MovimientoDTO;
import com.rvera.sofka.banksimulator.dto.MovimientoReporteDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface del servicio de Movimientos
 */
public interface IMovimientosService {
    List<MovimientoDTO> findAll();
    Optional<MovimientoDTO> findById(Long id);
    List<MovimientoDTO> findByCuentaId(Long cuentaId);
    MovimientoDTO save(MovimientoDTO movimientoDTO);
    void deleteById(Long id);
    List<MovimientoReporteDTO> findByClienteIdAndFechaBetween(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<MovimientoDTO> searchByFilter(String filtro);
}
