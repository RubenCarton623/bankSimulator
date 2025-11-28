package com.rvera.sofka.banksimulator.mapper;

import com.rvera.sofka.banksimulator.dto.CuentaDTO;
import com.rvera.sofka.banksimulator.entity.Cliente;
import com.rvera.sofka.banksimulator.entity.Cuenta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper para Cuenta
 */
@Component
@RequiredArgsConstructor
public class CuentaMapper {
    
    private final ClienteMapper clienteMapper;
    
    public CuentaDTO toDTO(Cuenta cuenta) {
        if (cuenta == null) {
            return null;
        }
        
        return CuentaDTO.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial())
                .estado(cuenta.getEstado())
                .clienteId(cuenta.getCliente() != null ? cuenta.getCliente().getClienteId() : null)
                .cliente(cuenta.getCliente() != null ? clienteMapper.toDTO(cuenta.getCliente()) : null)
                .build();
    }
    
    public Cuenta toEntity(CuentaDTO cuentaDTO) {
        if (cuentaDTO == null) {
            return null;
        }
        
        Cuenta cuenta = new Cuenta();
        cuenta.setId(cuentaDTO.getId());
        cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuenta.setEstado(cuentaDTO.getEstado());
        
        // El cliente se establece en el servicio
        if (cuentaDTO.getClienteId() != null) {
            Cliente cliente = new Cliente();
            cliente.setClienteId(cuentaDTO.getClienteId());
            cuenta.setCliente(cliente);
        }
        
        return cuenta;
    }
}
