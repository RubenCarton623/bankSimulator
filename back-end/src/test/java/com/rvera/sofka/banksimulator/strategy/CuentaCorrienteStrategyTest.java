package com.rvera.sofka.banksimulator.strategy;

import com.rvera.sofka.banksimulator.entity.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaCorrienteStrategyTest {

    private CuentaCorrienteStrategy strategy;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        strategy = new CuentaCorrienteStrategy();
        cuenta = new Cuenta();
        cuenta.setTipoCuenta("Corriente");
    }

    @Test
    void testGetTipoCuenta_ShouldReturnCorriente() {
        // Act
        String tipo = strategy.getTipoCuenta();

        // Assert
        assertEquals("Corriente", tipo);
    }

    @Test
    void testObtenerSaldoMinimo_ShouldReturn500() {
        // Act
        BigDecimal saldoMinimo = strategy.obtenerSaldoMinimo();

        // Assert
        assertEquals(new BigDecimal("500.00"), saldoMinimo);
    }

    @Test
    void testValidarCuenta_WithValidBalance_ShouldNotThrowException() {
        // Arrange
        cuenta.setSaldoInicial(new BigDecimal("500.00"));

        // Act & Assert
        assertDoesNotThrow(() -> strategy.validarCuenta(cuenta));
    }

    @Test
    void testValidarCuenta_WithBalanceAboveMinimum_ShouldNotThrowException() {
        // Arrange
        cuenta.setSaldoInicial(new BigDecimal("1000.00"));

        // Act & Assert
        assertDoesNotThrow(() -> strategy.validarCuenta(cuenta));
    }

    @Test
    void testValidarCuenta_WithBalanceBelowMinimum_ShouldThrowException() {
        // Arrange
        cuenta.setSaldoInicial(new BigDecimal("499.99"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            strategy.validarCuenta(cuenta);
        });

        assertTrue(exception.getMessage().contains("El saldo inicial para una cuenta corriente"));
        assertTrue(exception.getMessage().contains("500.00"));
    }

    @Test
    void testAplicarComision_ShouldAddCommission() {
        // Arrange - Cuentas corrientes tienen comisión de $2.00
        BigDecimal monto = new BigDecimal("200.00");
        BigDecimal expectedResult = new BigDecimal("202.00");

        // Act
        BigDecimal resultado = strategy.aplicarComision(monto);

        // Assert
        assertEquals(expectedResult, resultado);
    }

    @Test
    void testAplicarComision_WithDifferentAmounts_ShouldAddCommission() {
        // Test con varios montos
        BigDecimal monto1 = new BigDecimal("100.00");
        BigDecimal expected1 = new BigDecimal("102.00");
        assertEquals(expected1, strategy.aplicarComision(monto1));

        BigDecimal monto2 = new BigDecimal("500.00");
        BigDecimal expected2 = new BigDecimal("502.00");
        assertEquals(expected2, strategy.aplicarComision(monto2));

        BigDecimal monto3 = new BigDecimal("1000.00");
        BigDecimal expected3 = new BigDecimal("1002.00");
        assertEquals(expected3, strategy.aplicarComision(monto3));
    }

    @Test
    void testAplicarComision_WithSmallAmount_ShouldStillAddFullCommission() {
        // Arrange - Incluso con montos pequeños, se agrega la comisión completa
        BigDecimal monto = new BigDecimal("1.00");
        BigDecimal expectedResult = new BigDecimal("3.00"); // 1.00 + 2.00 comisión

        // Act
        BigDecimal resultado = strategy.aplicarComision(monto);

        // Assert
        assertEquals(expectedResult, resultado);
    }
}
