package com.rvera.sofka.banksimulator.repository;

import com.rvera.sofka.banksimulator.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByPersonaIdentificacion(String identificacion);
    
    // Métodos para filtrar solo activos
    @Query("SELECT c FROM Cliente c WHERE c.estado = true")
    List<Cliente> findAllActive();
    
    @Query("SELECT c FROM Cliente c WHERE c.clienteId = :id AND c.estado = true")
    Optional<Cliente> findByIdAndActive(Long id);
    
    @Query("SELECT c FROM Cliente c WHERE c.persona.identificacion = :identificacion AND c.estado = true")
    Optional<Cliente> findByPersonaIdentificacionAndActive(String identificacion);
    
    // Búsqueda general con LIKE
    @Query("SELECT c FROM Cliente c WHERE c.estado = true AND " +
           "(LOWER(c.persona.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.persona.identificacion) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.persona.direccion) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.persona.telefono) LIKE LOWER(CONCAT('%', :filtro, '%')))")
    List<Cliente> searchByFilter(@Param("filtro") String filtro);
}
