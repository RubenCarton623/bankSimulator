package com.rvera.sofka.banksimulator.controller;

import com.rvera.sofka.banksimulator.dto.MovimientoDTO;
import com.rvera.sofka.banksimulator.dto.MovimientoReporteDTO;
import com.rvera.sofka.banksimulator.service.IMovimientosService;
import com.rvera.sofka.banksimulator.service.IReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para Movimientos
 * Path base: /api/v1/movimientos
 * Aplica comisiones usando Strategy Pattern según tipo de cuenta
 */
@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientosController {
    
    private final IMovimientosService movimientosService;
    private final IReporteService reporteService;
    
    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> getAllMovimientos() {
        return ResponseEntity.ok(movimientosService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> getMovimientoById(@PathVariable Long id) {
        return movimientosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<MovimientoDTO>> getMovimientosByCuentaId(@PathVariable Long cuentaId) {
        return ResponseEntity.ok(movimientosService.findByCuentaId(cuentaId));
    }
    
    @PostMapping
    public ResponseEntity<MovimientoDTO> createMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO savedMovimiento = movimientosService.save(movimientoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovimiento);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        return movimientosService.findById(id)
                .map(movimiento -> {
                    movimientosService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Endpoint para consultar movimientos por rango de fechas y cliente
     * @param clienteId ID del cliente
     * @param fechaInicio Fecha y hora de inicio (formato: yyyy-MM-dd'T'HH:mm:ss)
     * @param fechaFin Fecha y hora de fin (formato: yyyy-MM-dd'T'HH:mm:ss)
     * @return Lista de movimientos en formato de reporte
     */
    @GetMapping("/reportes")
    public ResponseEntity<List<MovimientoReporteDTO>> getMovimientosByClienteAndFecha(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        List<MovimientoReporteDTO> movimientos = movimientosService.findByClienteIdAndFechaBetween(
                clienteId, fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);
    }
    
    /**
     * Endpoint para generar y descargar reporte PDF de movimientos
     * @param clienteId ID del cliente
     * @param fechaInicio Fecha y hora de inicio (formato: yyyy-MM-dd'T'HH:mm:ss)
     * @param fechaFin Fecha y hora de fin (formato: yyyy-MM-dd'T'HH:mm:ss)
     * @return PDF con el reporte de movimientos
     */
    @GetMapping("/reportes/pdf")
    public ResponseEntity<byte[]> getReporteMovimientosPdf(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        byte[] pdfBytes = reporteService.generarReporteMovimientos(clienteId, fechaInicio, fechaFin);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte-movimientos.pdf");
        headers.setContentLength(pdfBytes.length);
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
   
    @GetMapping("/buscar")
    public ResponseEntity<List<MovimientoDTO>> searchMovimientos(@RequestParam String filtro) {
        return ResponseEntity.ok(movimientosService.searchByFilter(filtro));
    }
}
