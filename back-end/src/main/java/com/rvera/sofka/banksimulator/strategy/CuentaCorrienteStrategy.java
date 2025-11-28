package com.rvera.sofka.banksimulator.strategy;

import com.rvera.sofka.banksimulator.entity.Cuenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estrategia para Cuenta Corriente
 * Implementa reglas específicas para cuentas corrientes
 */
@Component
public class CuentaCorrienteStrategy implements ICuentaStrategy {
    
    private static final BigDecimal SALDO_MINIMO = new BigDecimal("500.00");
    private static final BigDecimal COMISION_RETIRO = new BigDecimal("2.00"); // $2 por retiro
    
    @Override
    public void validarCuenta(Cuenta cuenta) {
        if (cuenta.getSaldoInicial().compareTo(SALDO_MINIMO) < 0) {
            throw new IllegalArgumentException(
                "El saldo inicial para una cuenta corriente debe ser mínimo $" + SALDO_MINIMO
            );
        }
    }
    
    @Override
    public BigDecimal aplicarComision(BigDecimal monto) {
        // Las cuentas corrientes tienen comisión por retiro
        return monto.add(COMISION_RETIRO);
    }
    
    @Override
    public BigDecimal obtenerSaldoMinimo() {
        return SALDO_MINIMO;
    }
    
    @Override
    public String getTipoCuenta() {
        return "Corriente";
    }
}
