package com.rvera.sofka.banksimulator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimientos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    @Column(name = "tipo_movimiento", nullable = false)
    private String tipoMovimiento;
    
    @Column(nullable = false)
    private BigDecimal valor;
    
    @Column(nullable = false)
    private BigDecimal saldo;
    
    @Column(nullable = false)
    private Boolean estado = true;
    
    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;
}
