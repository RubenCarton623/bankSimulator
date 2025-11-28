package com.rvera.sofka.banksimulator.mapper;

import com.rvera.sofka.banksimulator.dto.MovimientoDTO;
import com.rvera.sofka.banksimulator.entity.Cuenta;
import com.rvera.sofka.banksimulator.entity.Movimientos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper para Movimientos
 */
@Component
@RequiredArgsConstructor
public class MovimientoMapper {
    
    private final CuentaMapper cuentaMapper;
    
    public MovimientoDTO toDTO(Movimientos movimiento) {
        if (movimiento == null) {
            return null;
        }
        
        return MovimientoDTO.builder()
                .id(movimiento.getId())
                .fecha(movimiento.getFecha())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .valor(movimiento.getValor())
                .saldo(movimiento.getSaldo())
                .estado(movimiento.getEstado())
                .cuentaId(movimiento.getCuenta() != null ? movimiento.getCuenta().getId() : null)
                .cuenta(movimiento.getCuenta() != null ? cuentaMapper.toDTO(movimiento.getCuenta()) : null)
                .build();
    }
    
    public Movimientos toEntity(MovimientoDTO movimientoDTO) {
        if (movimientoDTO == null) {
            return null;
        }
        
        Movimientos movimiento = new Movimientos();
        movimiento.setId(movimientoDTO.getId());
        movimiento.setFecha(movimientoDTO.getFecha());
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setValor(movimientoDTO.getValor());
        movimiento.setSaldo(movimientoDTO.getSaldo());
        movimiento.setEstado(movimientoDTO.getEstado() != null ? movimientoDTO.getEstado() : true);
        
        // La cuenta se establece en el servicio
        if (movimientoDTO.getCuentaId() != null) {
            Cuenta cuenta = new Cuenta();
            cuenta.setId(movimientoDTO.getCuentaId());
            movimiento.setCuenta(cuenta);
        }
        
        return movimiento;
    }
}
