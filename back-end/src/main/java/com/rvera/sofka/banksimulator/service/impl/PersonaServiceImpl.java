package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.PersonaDTO;
import com.rvera.sofka.banksimulator.entity.Persona;
import com.rvera.sofka.banksimulator.mapper.PersonaMapper;
import com.rvera.sofka.banksimulator.repository.PersonaRepository;
import com.rvera.sofka.banksimulator.service.IPersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Persona
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements IPersonaService {
    
    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO> findAll() {
        log.debug("Buscando todas las personas");
        return personaRepository.findAll().stream()
                .map(personaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PersonaDTO> findById(Long id) {
        log.debug("Buscando persona por ID: {}", id);
        return personaRepository.findById(id)
                .map(personaMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PersonaDTO> findByIdentificacion(String identificacion) {
        log.debug("Buscando persona por identificación: {}", identificacion);
        return personaRepository.findByIdentificacion(identificacion)
                .map(personaMapper::toDTO);
    }
    
    @Override
    @Transactional
    public PersonaDTO save(PersonaDTO personaDTO) {
        log.debug("Guardando persona: {}", personaDTO.getNombre());
        
        // Validar que no exista otra persona con la misma identificación
        if (personaDTO.getId() == null) {
            personaRepository.findByIdentificacion(personaDTO.getIdentificacion())
                    .ifPresent(p -> {
                        throw new IllegalArgumentException("Ya existe una persona con la identificación: " + personaDTO.getIdentificacion());
                    });
        }
        
        Persona persona = personaMapper.toEntity(personaDTO);
        Persona savedPersona = personaRepository.save(persona);
        return personaMapper.toDTO(savedPersona);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Eliminando persona con ID: {}", id);
        personaRepository.deleteById(id);
    }
}
