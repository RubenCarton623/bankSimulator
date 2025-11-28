package com.rvera.sofka.banksimulator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cuenta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;
    
    @Column(name = "tipo_cuenta", nullable = false)
    private String tipoCuenta;
    
    @Column(name = "saldo_inicial", nullable = false)
    private BigDecimal saldoInicial;
    
    @Column(nullable = false)
    private Boolean estado = true;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
