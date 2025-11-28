package com.rvera.sofka.banksimulator.mapper;

import com.rvera.sofka.banksimulator.dto.ClienteDTO;
import com.rvera.sofka.banksimulator.entity.Cliente;
import com.rvera.sofka.banksimulator.entity.Persona;
import org.springframework.stereotype.Component;

/**
 * Mapper para Cliente con estructura plana
 */
@Component
public class ClienteMapper {
    
    public ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        Persona persona = cliente.getPersona();
        
        return ClienteDTO.builder()
                .clienteId(cliente.getClienteId())
                // Campos de Persona
                .nombre(persona.getNombre())
                .genero(persona.getGenero())
                .edad(persona.getEdad())
                .identificacion(persona.getIdentificacion())
                .direccion(persona.getDireccion())
                .telefono(persona.getTelefono())
                // Campos de Cliente
                .contrasena(cliente.getContrasena())
                .estado(cliente.getEstado())
                .build();
    }
    
    public Cliente toEntity(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return null;
        }
        
        // Crear Persona
        Persona persona = new Persona();
        persona.setNombre(clienteDTO.getNombre());
        persona.setGenero(clienteDTO.getGenero());
        persona.setEdad(clienteDTO.getEdad());
        persona.setIdentificacion(clienteDTO.getIdentificacion());
        persona.setDireccion(clienteDTO.getDireccion());
        persona.setTelefono(clienteDTO.getTelefono());
        
        // Crear Cliente
        Cliente cliente = new Cliente();
        cliente.setClienteId(clienteDTO.getClienteId());
        cliente.setPersona(persona);
        cliente.setContrasena(clienteDTO.getContrasena());
        cliente.setEstado(clienteDTO.getEstado());
        
        return cliente;
    }
    
    public void updateEntityFromDTO(ClienteDTO clienteDTO, Cliente cliente) {
        if (clienteDTO == null || cliente == null) {
            return;
        }
        
        // Actualizar Persona
        Persona persona = cliente.getPersona();
        if (persona == null) {
            persona = new Persona();
            cliente.setPersona(persona);
        }
        
        persona.setNombre(clienteDTO.getNombre());
        persona.setGenero(clienteDTO.getGenero());
        persona.setEdad(clienteDTO.getEdad());
        persona.setIdentificacion(clienteDTO.getIdentificacion());
        persona.setDireccion(clienteDTO.getDireccion());
        persona.setTelefono(clienteDTO.getTelefono());
        
        // Actualizar Cliente
        cliente.setContrasena(clienteDTO.getContrasena());
        cliente.setEstado(clienteDTO.getEstado());
    }
}
