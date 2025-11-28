package com.rvera.sofka.banksimulator.service;

import com.rvera.sofka.banksimulator.dto.CuentaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface del servicio de Cuenta
 */
public interface ICuentaService {
    List<CuentaDTO> findAll();
    Optional<CuentaDTO> findById(Long id);
    Optional<CuentaDTO> findByNumeroCuenta(String numeroCuenta);
    List<CuentaDTO> findByClienteId(Long clienteId);
    CuentaDTO save(CuentaDTO cuentaDTO);
    void deleteById(Long id);
    List<CuentaDTO> searchByFilter(String filtro);
}
