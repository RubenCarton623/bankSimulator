package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.MovimientoDTO;
import com.rvera.sofka.banksimulator.dto.MovimientoReporteDTO;
import com.rvera.sofka.banksimulator.entity.Cuenta;
import com.rvera.sofka.banksimulator.entity.Movimientos;
import com.rvera.sofka.banksimulator.mapper.MovimientoMapper;
import com.rvera.sofka.banksimulator.repository.CuentaRepository;
import com.rvera.sofka.banksimulator.repository.MovimientosRepository;
import com.rvera.sofka.banksimulator.service.IMovimientosService;
import com.rvera.sofka.banksimulator.strategy.CuentaStrategyFactory;
import com.rvera.sofka.banksimulator.strategy.ICuentaStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Movimientos
 * Aplica Strategy Pattern para calcular comisiones según tipo de cuenta
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MovimientosServiceImpl implements IMovimientosService {
    
    private final MovimientosRepository movimientosRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;
    private final CuentaStrategyFactory strategyFactory;
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> findAll() {
        log.debug("Buscando todos los movimientos activos");
        return movimientosRepository.findAllActive().stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientoDTO> findById(Long id) {
        log.debug("Buscando movimiento activo por ID: {}", id);
        return movimientosRepository.findByIdAndActive(id)
                .map(movimientoMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> findByCuentaId(Long cuentaId) {
        log.debug("Buscando movimientos activos de la cuenta ID: {}", cuentaId);
        return movimientosRepository.findByCuentaIdAndActiveOrderByFechaDesc(cuentaId).stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public MovimientoDTO save(MovimientoDTO movimientoDTO) {
        log.debug("Guardando movimiento: {}", movimientoDTO.getTipoMovimiento());
        
        // Validar que la cuenta exista
        Cuenta cuenta = cuentaRepository.findById(movimientoDTO.getCuentaId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + movimientoDTO.getCuentaId()));
        
        // Obtener último saldo
        BigDecimal saldoActual = obtenerUltimoSaldo(cuenta.getId());
        
        // Aplicar Strategy Pattern: calcular comisión según tipo de cuenta si es retiro
        BigDecimal valor = movimientoDTO.getValor();
        if ("Retiro".equals(movimientoDTO.getTipoMovimiento())) {
            ICuentaStrategy strategy = strategyFactory.getStrategy(cuenta.getTipoCuenta());
            valor = strategy.aplicarComision(valor.abs());
            valor = valor.negate(); // Los retiros son negativos
        }
        
        // Calcular nuevo saldo
        BigDecimal nuevoSaldo = saldoActual.add(valor);
        
        // Validar saldo suficiente
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el movimiento");
        }
        
        Movimientos movimiento = movimientoMapper.toEntity(movimientoDTO);
        movimiento.setCuenta(cuenta);
        movimiento.setValor(valor);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setFecha(LocalDateTime.now());
        
        Movimientos savedMovimiento = movimientosRepository.save(movimiento);
        return movimientoMapper.toDTO(savedMovimiento);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Eliminando lógicamente movimiento con ID: {}", id);
        Movimientos movimiento = movimientosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado con ID: " + id));
        movimiento.setEstado(false);
        movimientosRepository.save(movimiento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoReporteDTO> findByClienteIdAndFechaBetween(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Buscando movimientos del cliente ID: {} entre {} y {}", clienteId, fechaInicio, fechaFin);
        
        List<Movimientos> movimientos = movimientosRepository.findByClienteIdAndFechaBetween(
                clienteId, fechaInicio, fechaFin);
        
        return movimientos.stream()
                .map(this::mapToReporteDTO)
                .collect(Collectors.toList());
    }
    
    private MovimientoReporteDTO mapToReporteDTO(Movimientos movimiento) {
        // Convertir LocalDateTime a Date
        Date fechaDate = Date.from(movimiento.getFecha()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        
        return MovimientoReporteDTO.builder()
                .fecha(fechaDate)
                .cliente(movimiento.getCuenta().getCliente().getPersona().getNombre())
                .numeroCuenta(Long.parseLong(movimiento.getCuenta().getNumeroCuenta()))
                .tipo(movimiento.getCuenta().getTipoCuenta())
                .saldoInicial(movimiento.getCuenta().getSaldoInicial().floatValue())
                .estado(movimiento.getEstado())
                .movimiento(movimiento.getValor().floatValue())
                .saldoDisponible(movimiento.getSaldo().floatValue())
                .build();
    }
    
    private BigDecimal obtenerUltimoSaldo(Long cuentaId) {
        List<Movimientos> movimientos = movimientosRepository.findByCuentaIdAndActiveOrderByFechaDesc(cuentaId);
        if (movimientos.isEmpty()) {
            // Si no hay movimientos, retornar el saldo inicial de la cuenta
            return cuentaRepository.findById(cuentaId)
                    .map(Cuenta::getSaldoInicial)
                    .orElse(BigDecimal.ZERO);
        }
        return movimientos.get(0).getSaldo();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> searchByFilter(String filtro) {
        log.debug("Buscando movimientos con filtro: {}", filtro);
        if (filtro == null || filtro.trim().isEmpty()) {
            return findAll();
        }
        return movimientosRepository.searchByFilter(filtro).stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
