package com.rvera.sofka.banksimulator.repository;

import com.rvera.sofka.banksimulator.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteClienteId(Long clienteId);
    
    // Métodos para filtrar solo activas
    @Query("SELECT c FROM Cuenta c WHERE c.estado = true")
    List<Cuenta> findAllActive();
    
    @Query("SELECT c FROM Cuenta c WHERE c.id = :id AND c.estado = true")
    Optional<Cuenta> findByIdAndActive(Long id);
    
    @Query("SELECT c FROM Cuenta c WHERE c.numeroCuenta = :numeroCuenta AND c.estado = true")
    Optional<Cuenta> findByNumeroCuentaAndActive(String numeroCuenta);
    
    @Query("SELECT c FROM Cuenta c WHERE c.cliente.clienteId = :clienteId AND c.estado = true")
    List<Cuenta> findByClienteClienteIdAndActive(Long clienteId);
    
    // Búsqueda general con LIKE
    @Query("SELECT c FROM Cuenta c WHERE c.estado = true AND " +
           "(LOWER(c.numeroCuenta) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.tipoCuenta) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.cliente.persona.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.cliente.persona.identificacion) LIKE LOWER(CONCAT('%', :filtro, '%')))")
    List<Cuenta> searchByFilter(@Param("filtro") String filtro);
}
