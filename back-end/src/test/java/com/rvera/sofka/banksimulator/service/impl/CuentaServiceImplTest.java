package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.CuentaDTO;
import com.rvera.sofka.banksimulator.entity.Cliente;
import com.rvera.sofka.banksimulator.entity.Cuenta;
import com.rvera.sofka.banksimulator.mapper.CuentaMapper;
import com.rvera.sofka.banksimulator.repository.ClienteRepository;
import com.rvera.sofka.banksimulator.repository.CuentaRepository;
import com.rvera.sofka.banksimulator.strategy.CuentaStrategyFactory;
import com.rvera.sofka.banksimulator.strategy.ICuentaStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CuentaMapper cuentaMapper;

    @Mock
    private CuentaStrategyFactory strategyFactory;

    @Mock
    private ICuentaStrategy cuentaStrategy;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private Cuenta cuenta;
    private CuentaDTO cuentaDTO;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Setup Cliente
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setContrasena("password123");
        cliente.setEstado(true);

        // Setup Cuenta entity
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(new BigDecimal("1000.00"));
        cuenta.setEstado(true);
        cuenta.setCliente(cliente);

        // Setup CuentaDTO
        cuentaDTO = CuentaDTO.builder()
                .id(1L)
                .numeroCuenta("1234567890")
                .tipoCuenta("Ahorros")
                .saldoInicial(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId(1L)
                .build();
    }

    @Test
    void testFindAll_ShouldReturnListOfCuentas() {
        // Arrange
        List<Cuenta> cuentas = Arrays.asList(cuenta);
        when(cuentaRepository.findAllActive()).thenReturn(cuentas);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // Act
        List<CuentaDTO> result = cuentaService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cuentaRepository, times(1)).findAllActive();
        verify(cuentaMapper, times(1)).toDTO(any(Cuenta.class));
    }

    @Test
    void testFindById_WhenCuentaExists_ShouldReturnCuenta() {
        // Arrange
        when(cuentaRepository.findByIdAndActive(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // Act
        Optional<CuentaDTO> result = cuentaService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1234567890", result.get().getNumeroCuenta());
        assertEquals("Ahorros", result.get().getTipoCuenta());
        verify(cuentaRepository, times(1)).findByIdAndActive(1L);
    }

    @Test
    void testFindById_WhenCuentaNotExists_ShouldReturnEmpty() {
        // Arrange
        when(cuentaRepository.findByIdAndActive(999L)).thenReturn(Optional.empty());

        // Act
        Optional<CuentaDTO> result = cuentaService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(cuentaRepository, times(1)).findByIdAndActive(999L);
    }

    @Test
    void testFindByNumeroCuenta_WhenCuentaExists_ShouldReturnCuenta() {
        // Arrange
        when(cuentaRepository.findByNumeroCuentaAndActive("1234567890")).thenReturn(Optional.of(cuenta));
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // Act
        Optional<CuentaDTO> result = cuentaService.findByNumeroCuenta("1234567890");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1234567890", result.get().getNumeroCuenta());
        verify(cuentaRepository, times(1)).findByNumeroCuentaAndActive("1234567890");
    }

    @Test
    void testFindByClienteId_ShouldReturnListOfCuentas() {
        // Arrange
        List<Cuenta> cuentas = Arrays.asList(cuenta);
        when(cuentaRepository.findByClienteClienteIdAndActive(1L)).thenReturn(cuentas);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // Act
        List<CuentaDTO> result = cuentaService.findByClienteId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cuentaRepository, times(1)).findByClienteClienteIdAndActive(1L);
    }

    @Test
    void testSave_NewCuenta_ShouldSaveAndReturnCuenta() {
        // Arrange
        CuentaDTO newCuentaDTO = CuentaDTO.builder()
                .numeroCuenta("9876543210")
                .tipoCuenta("Corriente")
                .saldoInicial(new BigDecimal("2000.00"))
                .estado(true)
                .clienteId(1L)
                .build();

        when(cuentaRepository.findByNumeroCuenta("9876543210")).thenReturn(Optional.empty());
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(cuentaMapper.toEntity(any(CuentaDTO.class))).thenReturn(cuenta);
        when(strategyFactory.getStrategy(anyString())).thenReturn(cuentaStrategy);
        doNothing().when(cuentaStrategy).validarCuenta(any(Cuenta.class));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(newCuentaDTO);

        // Act
        CuentaDTO result = cuentaService.save(newCuentaDTO);

        // Assert
        assertNotNull(result);
        verify(cuentaRepository, times(1)).findByNumeroCuenta("9876543210");
        verify(clienteRepository, times(1)).findById(1L);
        verify(cuentaStrategy, times(1)).validarCuenta(any(Cuenta.class));
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testSave_DuplicateNumeroCuenta_ShouldThrowException() {
        // Arrange
        CuentaDTO newCuentaDTO = CuentaDTO.builder()
                .numeroCuenta("1234567890") // Número duplicado
                .tipoCuenta("Ahorros")
                .saldoInicial(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId(1L)
                .build();

        when(cuentaRepository.findByNumeroCuenta("1234567890")).thenReturn(Optional.of(cuenta));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.save(newCuentaDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe una cuenta con el número"));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testSave_ClienteNotFound_ShouldThrowException() {
        // Arrange
        CuentaDTO newCuentaDTO = CuentaDTO.builder()
                .numeroCuenta("9876543210")
                .tipoCuenta("Ahorros")
                .saldoInicial(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId(999L) // Cliente inexistente
                .build();

        when(cuentaRepository.findByNumeroCuenta("9876543210")).thenReturn(Optional.empty());
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.save(newCuentaDTO);
        });

        assertTrue(exception.getMessage().contains("Cliente no encontrado"));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testDeleteById_ShouldCallRepository() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        
        // Act
        cuentaService.deleteById(1L);

        // Assert
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }
}
