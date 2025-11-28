package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.MovimientoReporteDTO;
import com.rvera.sofka.banksimulator.entity.Movimientos;
import com.rvera.sofka.banksimulator.repository.MovimientosRepository;
import com.rvera.sofka.banksimulator.service.IReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de reportes PDF usando JasperReports
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final MovimientosRepository movimientosRepository;

    @Override
    @Transactional(readOnly = true)
    public byte[] generarReporteMovimientos(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Generando reporte PDF de movimientos para cliente ID: {} entre {} y {}", 
                clienteId, fechaInicio, fechaFin);
        
        try {
            // Obtener los movimientos del repositorio
            List<Movimientos> movimientos = movimientosRepository.findByClienteIdAndFechaBetween(
                    clienteId, fechaInicio, fechaFin);
            
            if (movimientos.isEmpty()) {
                log.warn("No se encontraron movimientos para el cliente ID: {} en el rango de fechas especificado", clienteId);
            }
            
            // Convertir a DTOs para el reporte
            List<Map<String, Object>> reportData = movimientos.stream()
                    .map(this::mapToReportData)
                    .collect(Collectors.toList());
            
            // Cargar el archivo .jrxml
            InputStream reportStream = new ClassPathResource("reports/movimientos-report.jrxml")
                    .getInputStream();
            
            // Compilar el reporte
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            // Crear el data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
            
            // Parámetros del reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("movimientos", dataSource);
            
            // Llenar el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, 
                    parameters, 
                    new JREmptyDataSource()
            );
            
            // Exportar a PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            
            log.info("Reporte PDF generado exitosamente con {} movimientos", reportData.size());
            return pdfBytes;
            
        } catch (Exception e) {
            log.error("Error al generar reporte PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar el reporte PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mapea una entidad Movimiento a un Map para el reporte JasperReports
     */
    private Map<String, Object> mapToReportData(Movimientos movimiento) {
        Map<String, Object> data = new HashMap<>();
        
        // Convertir LocalDateTime a Date para JasperReports
        Date fechaDate = Date.from(movimiento.getFecha()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        
        data.put("movimientoDate", fechaDate);
        data.put("cliente", movimiento.getCuenta().getCliente().getPersona().getNombre());
        data.put("numeroCuenta", Long.parseLong(movimiento.getCuenta().getNumeroCuenta()));
        data.put("tipo", movimiento.getCuenta().getTipoCuenta());
        data.put("saldoInicial", movimiento.getCuenta().getSaldoInicial().floatValue());
        data.put("estado", movimiento.getEstado());
        data.put("movimiento", movimiento.getValor().floatValue());
        data.put("saldoDisponible", movimiento.getSaldo().floatValue());
        
        return data;
    }
}
