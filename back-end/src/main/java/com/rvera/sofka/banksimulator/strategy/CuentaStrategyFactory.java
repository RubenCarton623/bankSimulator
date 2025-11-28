package com.rvera.sofka.banksimulator.strategy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory para seleccionar la estrategia correcta según el tipo de cuenta
 * Cumple con DRY: no repite lógica de selección de estrategia
 */
@Component
public class CuentaStrategyFactory {
    
    private final Map<String, ICuentaStrategy> strategies;
    
    public CuentaStrategyFactory(List<ICuentaStrategy> strategyList) {
        this.strategies = new HashMap<>();
        for (ICuentaStrategy strategy : strategyList) {
            strategies.put(strategy.getTipoCuenta(), strategy);
        }
    }
    
    public ICuentaStrategy getStrategy(String tipoCuenta) {
        ICuentaStrategy strategy = strategies.get(tipoCuenta);
        if (strategy == null) {
            throw new IllegalArgumentException("Tipo de cuenta no soportado: " + tipoCuenta);
        }
        return strategy;
    }
}
