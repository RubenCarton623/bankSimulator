package com.rvera.sofka.banksimulator.controller;

import com.rvera.sofka.banksimulator.dto.CuentaDTO;
import com.rvera.sofka.banksimulator.service.ICuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Cuenta
 * Path base: /api/v1/cuentas
 * Usa Strategy Pattern en el servicio para validar tipos de cuenta
 */
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    
    private final ICuentaService cuentaService;
    
    @GetMapping
    public ResponseEntity<List<CuentaDTO>> getAllCuentas() {
        return ResponseEntity.ok(cuentaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> getCuentaById(@PathVariable Long id) {
        return cuentaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaDTO> getCuentaByNumero(@PathVariable String numeroCuenta) {
        return cuentaService.findByNumeroCuenta(numeroCuenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaDTO>> getCuentasByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(cuentaService.findByClienteId(clienteId));
    }
    
    @PostMapping
    public ResponseEntity<CuentaDTO> createCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO savedCuenta = cuentaService.save(cuentaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCuenta);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> updateCuenta(
            @PathVariable Long id, 
            @Valid @RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.findById(id)
                .map(existingCuenta -> {
                    cuentaDTO.setId(id);
                    return ResponseEntity.ok(cuentaService.save(cuentaDTO));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        return cuentaService.findById(id)
                .map(cuenta -> {
                    cuentaService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<CuentaDTO>> searchCuentas(@RequestParam String filtro) {
        return ResponseEntity.ok(cuentaService.searchByFilter(filtro));
    }
}
