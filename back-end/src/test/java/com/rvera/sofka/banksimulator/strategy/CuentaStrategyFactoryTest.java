package com.rvera.sofka.banksimulator.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CuentaStrategyFactoryTest {

    private CuentaAhorrosStrategy cuentaAhorrosStrategy;
    private CuentaCorrienteStrategy cuentaCorrienteStrategy;
    private CuentaStrategyFactory factory;

    @BeforeEach
    void setUp() {
        cuentaAhorrosStrategy = new CuentaAhorrosStrategy();
        cuentaCorrienteStrategy = new CuentaCorrienteStrategy();
        List<ICuentaStrategy> strategies = Arrays.asList(cuentaAhorrosStrategy, cuentaCorrienteStrategy);
        factory = new CuentaStrategyFactory(strategies);
    }

    @Test
    void testGetStrategy_Ahorros_ShouldReturnAhorrosStrategy() {
        // Act
        ICuentaStrategy strategy = factory.getStrategy("Ahorros");

        // Assert
        assertNotNull(strategy);
        assertEquals(cuentaAhorrosStrategy, strategy);
    }

    @Test
    void testGetStrategy_Corriente_ShouldReturnCorrienteStrategy() {
        // Act
        ICuentaStrategy strategy = factory.getStrategy("Corriente");

        // Assert
        assertNotNull(strategy);
        assertEquals(cuentaCorrienteStrategy, strategy);
    }

    @Test
    void testGetStrategy_InvalidType_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.getStrategy("Invalid");
        });

        assertTrue(exception.getMessage().contains("Tipo de cuenta no soportado"));
    }

    @Test
    void testGetStrategy_Null_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.getStrategy(null);
        });

        assertTrue(exception.getMessage().contains("Tipo de cuenta no soportado"));
    }
}
