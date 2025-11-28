package com.rvera.sofka.banksimulator.mapper;

import com.rvera.sofka.banksimulator.dto.PersonaDTO;
import com.rvera.sofka.banksimulator.entity.Persona;
import org.springframework.stereotype.Component;

/**
 * Mapper para Persona
 */
@Component
public class PersonaMapper {
    
    public PersonaDTO toDTO(Persona persona) {
        if (persona == null) {
            return null;
        }
        
        return PersonaDTO.builder()
                .id(persona.getId())
                .nombre(persona.getNombre())
                .genero(persona.getGenero())
                .edad(persona.getEdad())
                .identificacion(persona.getIdentificacion())
                .direccion(persona.getDireccion())
                .telefono(persona.getTelefono())
                .build();
    }
    
    public Persona toEntity(PersonaDTO personaDTO) {
        if (personaDTO == null) {
            return null;
        }
        
        Persona persona = new Persona();
        persona.setId(personaDTO.getId());
        persona.setNombre(personaDTO.getNombre());
        persona.setGenero(personaDTO.getGenero());
        persona.setEdad(personaDTO.getEdad());
        persona.setIdentificacion(personaDTO.getIdentificacion());
        persona.setDireccion(personaDTO.getDireccion());
        persona.setTelefono(personaDTO.getTelefono());
        
        return persona;
    }
    
    public void updateEntityFromDTO(PersonaDTO personaDTO, Persona persona) {
        if (personaDTO == null || persona == null) {
            return;
        }
        
        if (personaDTO.getNombre() != null) {
            persona.setNombre(personaDTO.getNombre());
        }
        if (personaDTO.getGenero() != null) {
            persona.setGenero(personaDTO.getGenero());
        }
        if (personaDTO.getEdad() != null) {
            persona.setEdad(personaDTO.getEdad());
        }
        if (personaDTO.getDireccion() != null) {
            persona.setDireccion(personaDTO.getDireccion());
        }
        if (personaDTO.getTelefono() != null) {
            persona.setTelefono(personaDTO.getTelefono());
        }
    }
}
