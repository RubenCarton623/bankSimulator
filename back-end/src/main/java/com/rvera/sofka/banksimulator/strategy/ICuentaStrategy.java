package com.rvera.sofka.banksimulator.strategy;

import com.rvera.sofka.banksimulator.entity.Cuenta;

import java.math.BigDecimal;

/**
 * Strategy Pattern - Interface
 * Define el contrato para diferentes estrategias de procesamiento de cuentas
 */
public interface ICuentaStrategy {
    
    /**
     * Valida las reglas específicas del tipo de cuenta
     */
    void validarCuenta(Cuenta cuenta);
    
    /**
     * Aplica comisiones o beneficios específicos del tipo de cuenta
     */
    BigDecimal aplicarComision(BigDecimal monto);
    
    /**
     * Obtiene el saldo mínimo requerido para el tipo de cuenta
     */
    BigDecimal obtenerSaldoMinimo();
    
    /**
     * Retorna el tipo de cuenta que maneja esta estrategia
     */
    String getTipoCuenta();
}
