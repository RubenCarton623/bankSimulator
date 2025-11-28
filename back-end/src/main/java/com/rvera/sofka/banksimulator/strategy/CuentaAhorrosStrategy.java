package com.rvera.sofka.banksimulator.strategy;

import com.rvera.sofka.banksimulator.entity.Cuenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estrategia para Cuenta de Ahorros
 * Implementa reglas específicas para cuentas de ahorro
 */
@Component
public class CuentaAhorrosStrategy implements ICuentaStrategy {
    
    private static final BigDecimal SALDO_MINIMO = new BigDecimal("100.00");
    
    @Override
    public void validarCuenta(Cuenta cuenta) {
        if (cuenta.getSaldoInicial().compareTo(SALDO_MINIMO) < 0) {
            throw new IllegalArgumentException(
                "El saldo inicial para una cuenta de ahorros debe ser mínimo $" + SALDO_MINIMO
            );
        }
    }
    
    @Override
    public BigDecimal aplicarComision(BigDecimal monto) {
        // Las cuentas de ahorro no tienen comisión por retiros
        return monto;
    }
    
    @Override
    public BigDecimal obtenerSaldoMinimo() {
        return SALDO_MINIMO;
    }
    
    @Override
    public String getTipoCuenta() {
        return "Ahorros";
    }
}
