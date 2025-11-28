package com.rvera.sofka.banksimulator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    private String genero;
    
    private Integer edad;
    
    @Column(unique = true, nullable = false)
    private String identificacion;
    
    private String direccion;
    
    private String telefono;
}
