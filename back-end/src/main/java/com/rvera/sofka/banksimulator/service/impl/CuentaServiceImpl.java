package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.CuentaDTO;
import com.rvera.sofka.banksimulator.entity.Cliente;
import com.rvera.sofka.banksimulator.entity.Cuenta;
import com.rvera.sofka.banksimulator.mapper.CuentaMapper;
import com.rvera.sofka.banksimulator.repository.ClienteRepository;
import com.rvera.sofka.banksimulator.repository.CuentaRepository;
import com.rvera.sofka.banksimulator.service.ICuentaService;
import com.rvera.sofka.banksimulator.strategy.CuentaStrategyFactory;
import com.rvera.sofka.banksimulator.strategy.ICuentaStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Cuenta
 * Aplica Strategy Pattern para manejar diferentes tipos de cuenta
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements ICuentaService {
    
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final CuentaMapper cuentaMapper;
    private final CuentaStrategyFactory strategyFactory;
    
    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> findAll() {
        log.debug("Buscando todas las cuentas activas");
        return cuentaRepository.findAllActive().stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaDTO> findById(Long id) {
        log.debug("Buscando cuenta activa por ID: {}", id);
        return cuentaRepository.findByIdAndActive(id)
                .map(cuentaMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaDTO> findByNumeroCuenta(String numeroCuenta) {
        log.debug("Buscando cuenta activa por número: {}", numeroCuenta);
        return cuentaRepository.findByNumeroCuentaAndActive(numeroCuenta)
                .map(cuentaMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> findByClienteId(Long clienteId) {
        log.debug("Buscando cuentas activas del cliente ID: {}", clienteId);
        return cuentaRepository.findByClienteClienteIdAndActive(clienteId).stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CuentaDTO save(CuentaDTO cuentaDTO) {
        log.debug("Guardando cuenta: {}", cuentaDTO.getNumeroCuenta());
        
        // Validar que no exista otra cuenta con el mismo número
        if (cuentaDTO.getId() == null) {
            cuentaRepository.findByNumeroCuenta(cuentaDTO.getNumeroCuenta())
                    .ifPresent(c -> {
                        throw new IllegalArgumentException("Ya existe una cuenta con el número: " + cuentaDTO.getNumeroCuenta());
                    });
        }
        
        // Validar que el cliente exista
        Cliente cliente = clienteRepository.findById(cuentaDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + cuentaDTO.getClienteId()));
        
        Cuenta cuenta = cuentaMapper.toEntity(cuentaDTO);
        cuenta.setCliente(cliente);
        
        // Aplicar Strategy Pattern: validar reglas según tipo de cuenta
        ICuentaStrategy strategy = strategyFactory.getStrategy(cuenta.getTipoCuenta());
        strategy.validarCuenta(cuenta);
        
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return cuentaMapper.toDTO(savedCuenta);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Eliminando lógicamente cuenta con ID: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + id));
        cuenta.setEstado(false);
        cuentaRepository.save(cuenta);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> searchByFilter(String filtro) {
        log.debug("Buscando cuentas con filtro: {}", filtro);
        if (filtro == null || filtro.trim().isEmpty()) {
            return findAll();
        }
        return cuentaRepository.searchByFilter(filtro).stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
