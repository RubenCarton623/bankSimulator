package com.rvera.sofka.banksimulator.service;

import com.rvera.sofka.banksimulator.dto.ClienteDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface del servicio de Cliente
 */
public interface IClienteService {
    List<ClienteDTO> findAll();
    Optional<ClienteDTO> findById(Long id);
    Optional<ClienteDTO> findByIdentificacion(String identificacion);
    ClienteDTO save(ClienteDTO clienteDTO);
    void deleteById(Long id);
    List<ClienteDTO> searchByFilter(String filtro);
}
