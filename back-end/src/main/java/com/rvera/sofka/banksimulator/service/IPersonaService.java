package com.rvera.sofka.banksimulator.service;

import com.rvera.sofka.banksimulator.dto.PersonaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface del servicio de Persona
 */
public interface IPersonaService {
    List<PersonaDTO> findAll();

    Optional<PersonaDTO> findById(Long id);

    Optional<PersonaDTO> findByIdentificacion(String identificacion);

    PersonaDTO save(PersonaDTO personaDTO);

    void deleteById(Long id);
}
