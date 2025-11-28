package com.rvera.sofka.banksimulator.controller;

import com.rvera.sofka.banksimulator.dto.PersonaDTO;
import com.rvera.sofka.banksimulator.service.IPersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Persona
 * Path base: /api/v1/personas
 */
@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class PersonaController {
    
    private final IPersonaService personaService;
    
    @GetMapping
    public ResponseEntity<List<PersonaDTO>> getAllPersonas() {
        return ResponseEntity.ok(personaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> getPersonaById(@PathVariable Long id) {
        return personaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<PersonaDTO> getPersonaByIdentificacion(@PathVariable String identificacion) {
        return personaService.findByIdentificacion(identificacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PersonaDTO> createPersona(@Valid @RequestBody PersonaDTO personaDTO) {
        PersonaDTO savedPersona = personaService.save(personaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPersona);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTO> updatePersona(
            @PathVariable Long id, 
            @Valid @RequestBody PersonaDTO personaDTO) {
        return personaService.findById(id)
                .map(existingPersona -> {
                    personaDTO.setId(id);
                    return ResponseEntity.ok(personaService.save(personaDTO));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        return personaService.findById(id)
                .map(persona -> {
                    personaService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
