package com.rvera.sofka.banksimulator.repository;

import com.rvera.sofka.banksimulator.entity.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {
    List<Movimientos> findByCuentaId(Long cuentaId);
    List<Movimientos> findByCuentaIdOrderByFechaDesc(Long cuentaId);
    
    // Métodos para filtrar solo activos
    @Query("SELECT m FROM Movimientos m WHERE m.estado = true")
    List<Movimientos> findAllActive();
    
    @Query("SELECT m FROM Movimientos m WHERE m.id = :id AND m.estado = true")
    Optional<Movimientos> findByIdAndActive(Long id);
    
    @Query("SELECT m FROM Movimientos m WHERE m.cuenta.id = :cuentaId AND m.estado = true ORDER BY m.fecha DESC")
    List<Movimientos> findByCuentaIdAndActiveOrderByFechaDesc(Long cuentaId);
    
    // Consulta de movimientos por rango de fechas y cliente
    @Query("SELECT m FROM Movimientos m " +
           "WHERE m.cuenta.cliente.clienteId = :clienteId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "AND m.estado = true " +
           "ORDER BY m.fecha DESC")
    List<Movimientos> findByClienteIdAndFechaBetween(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
    
    // Búsqueda general con LIKE
    @Query("SELECT m FROM Movimientos m WHERE m.estado = true AND " +
           "(LOWER(m.tipoMovimiento) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(m.cuenta.numeroCuenta) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(m.cuenta.cliente.persona.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "CAST(m.valor AS string) LIKE CONCAT('%', :filtro, '%') OR " +
           "CAST(m.saldo AS string) LIKE CONCAT('%', :filtro, '%'))")
    List<Movimientos> searchByFilter(@Param("filtro") String filtro);
}
