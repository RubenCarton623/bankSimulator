package com.rvera.sofka.banksimulator.controller;

import com.rvera.sofka.banksimulator.dto.ClienteDTO;
import com.rvera.sofka.banksimulator.service.IClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Cliente
 * Path base: /api/v1/clientes
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    
    private final IClienteService clienteService;
    
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        return ResponseEntity.ok(clienteService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ClienteDTO> getClienteByIdentificacion(@PathVariable String identificacion) {
        return clienteService.findByIdentificacion(identificacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO savedCliente = clienteService.save(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(
            @PathVariable Long id, 
            @Valid @RequestBody ClienteDTO clienteDTO) {
        return clienteService.findById(id)
                .map(existingCliente -> {
                    clienteDTO.setClienteId(id);
                    return ResponseEntity.ok(clienteService.save(clienteDTO));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(cliente -> {
                    clienteService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteDTO>> searchClientes(@RequestParam String filtro) {
        return ResponseEntity.ok(clienteService.searchByFilter(filtro));
    }
}
