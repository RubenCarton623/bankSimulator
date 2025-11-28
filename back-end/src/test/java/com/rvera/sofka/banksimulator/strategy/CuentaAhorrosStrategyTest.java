package com.rvera.sofka.banksimulator.strategy;

import com.rvera.sofka.banksimulator.entity.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaAhorrosStrategyTest {

    private CuentaAhorrosStrategy strategy;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        strategy = new CuentaAhorrosStrategy();
        cuenta = new Cuenta();
        cuenta.setTipoCuenta("Ahorros");
    }

    @Test
    void testGetTipoCuenta_ShouldReturnAhorros() {
        // Act
        String tipo = strategy.getTipoCuenta();

        // Assert
        assertEquals("Ahorros", tipo);
    }

    @Test
    void testObtenerSaldoMinimo_ShouldReturn100() {
        // Act
        BigDecimal saldoMinimo = strategy.obtenerSaldoMinimo();

        // Assert
        assertEquals(new BigDecimal("100.00"), saldoMinimo);
    }

    @Test
    void testValidarCuenta_WithValidBalance_ShouldNotThrowException() {
        // Arrange
        cuenta.setSaldoInicial(new BigDecimal("100.00"));

        // Act & Assert
        assertDoesNotThrow(() -> strategy.validarCuenta(cuenta));
    }

    @Test
    void testValidarCuenta_WithBalanceAboveMinimum_ShouldNotThrowException() {
        // Arrange
        cuenta.setSaldoInicial(new BigDecimal("500.00"));

        // Act & Assert
        assertDoesNotThrow(() -> strategy.validarCuenta(cuenta));
    }

    @Test
    void testValidarCuenta_WithBalanceBelowMinimum_ShouldThrowException() {
        // Arrange
        cuenta.setSaldoInicial(new BigDecimal("99.99"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            strategy.validarCuenta(cuenta);
        });

        assertTrue(exception.getMessage().contains("El saldo inicial para una cuenta de ahorros"));
        assertTrue(exception.getMessage().contains("100.00"));
    }

    @Test
    void testAplicarComision_ShouldReturnSameAmount() {
        // Arrange - Cuentas de ahorro no tienen comisión
        BigDecimal monto = new BigDecimal("200.00");

        // Act
        BigDecimal resultado = strategy.aplicarComision(monto);

        // Assert
        assertEquals(monto, resultado);
    }

    @Test
    void testAplicarComision_WithDifferentAmounts_ShouldReturnSameAmount() {
        // Test con varios montos
        BigDecimal[] montos = {
            new BigDecimal("100.00"),
            new BigDecimal("500.00"),
            new BigDecimal("1000.00")
        };

        for (BigDecimal monto : montos) {
            assertEquals(monto, strategy.aplicarComision(monto));
        }
    }
}
