package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.ClienteDTO;
import com.rvera.sofka.banksimulator.entity.Cliente;
import com.rvera.sofka.banksimulator.mapper.ClienteMapper;
import com.rvera.sofka.banksimulator.repository.ClienteRepository;
import com.rvera.sofka.banksimulator.service.IClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Cliente
 * Cumple con SRP: responsabilidad única de lógica de negocio de clientes
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService {
    
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        log.debug("Buscando todos los clientes activos");
        return clienteRepository.findAllActive().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> findById(Long id) {
        log.debug("Buscando cliente activo por ID: {}", id);
        return clienteRepository.findByIdAndActive(id)
                .map(clienteMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> findByIdentificacion(String identificacion) {
        log.debug("Buscando cliente activo por identificación: {}", identificacion);
        return clienteRepository.findByPersonaIdentificacionAndActive(identificacion)
                .map(clienteMapper::toDTO);
    }
    
    @Override
    @Transactional
    public ClienteDTO save(ClienteDTO clienteDTO) {
        log.debug("Guardando cliente: {}", clienteDTO.getNombre());
        
        // Validar que no exista otro cliente con la misma identificación
        if (clienteDTO.getClienteId() == null) {
            clienteRepository.findByPersonaIdentificacion(clienteDTO.getIdentificacion())
                    .ifPresent(c -> {
                        throw new IllegalArgumentException("Ya existe un cliente con la identificación: " + clienteDTO.getIdentificacion());
                    });
        }
        
        Cliente cliente;
        if (clienteDTO.getClienteId() != null) {
            // Actualización: buscar entidad existente
            cliente = clienteRepository.findById(clienteDTO.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteDTO.getClienteId()));
            clienteMapper.updateEntityFromDTO(clienteDTO, cliente);
        } else {
            // Creación: nueva entidad
            cliente = clienteMapper.toEntity(clienteDTO);
        }
        
        Cliente savedCliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(savedCliente);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Eliminando lógicamente cliente con ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        cliente.setEstado(false);
        clienteRepository.save(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> searchByFilter(String filtro) {
        log.debug("Buscando clientes con filtro: {}", filtro);
        if (filtro == null || filtro.trim().isEmpty()) {
            return findAll();
        }
        return clienteRepository.searchByFilter(filtro).stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }
}
