package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.MovimientoDTO;
import com.rvera.sofka.banksimulator.entity.Cuenta;
import com.rvera.sofka.banksimulator.entity.Movimientos;
import com.rvera.sofka.banksimulator.mapper.MovimientoMapper;
import com.rvera.sofka.banksimulator.repository.CuentaRepository;
import com.rvera.sofka.banksimulator.repository.MovimientosRepository;
import com.rvera.sofka.banksimulator.strategy.CuentaStrategyFactory;
import com.rvera.sofka.banksimulator.strategy.ICuentaStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientosServiceImplTest {

    @Mock
    private MovimientosRepository movimientosRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoMapper movimientoMapper;

    @Mock
    private CuentaStrategyFactory strategyFactory;

    @Mock
    private ICuentaStrategy cuentaStrategy;

    @InjectMocks
    private MovimientosServiceImpl movimientosService;

    private Movimientos movimiento;
    private MovimientoDTO movimientoDTO;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        // Setup Cuenta
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(new BigDecimal("1000.00"));
        cuenta.setEstado(true);

        // Setup Movimiento entity
        movimiento = new Movimientos();
        movimiento.setId(1L);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento("Deposito");
        movimiento.setValor(new BigDecimal("500.00"));
        movimiento.setSaldo(new BigDecimal("1500.00"));
        movimiento.setCuenta(cuenta);

        // Setup MovimientoDTO
        movimientoDTO = MovimientoDTO.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .tipoMovimiento("Deposito")
                .valor(new BigDecimal("500.00"))
                .saldo(new BigDecimal("1500.00"))
                .cuentaId(1L)
                .build();
    }

    @Test
    void testFindAll_ShouldReturnListOfMovimientos() {
        // Arrange
        List<Movimientos> movimientos = Arrays.asList(movimiento);
        when(movimientosRepository.findAllActive()).thenReturn(movimientos);
        when(movimientoMapper.toDTO(any(Movimientos.class))).thenReturn(movimientoDTO);

        // Act
        List<MovimientoDTO> result = movimientosService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(movimientosRepository, times(1)).findAllActive();
        verify(movimientoMapper, times(1)).toDTO(any(Movimientos.class));
    }

    @Test
    void testFindById_WhenMovimientoExists_ShouldReturnMovimiento() {
        // Arrange
        when(movimientosRepository.findByIdAndActive(1L)).thenReturn(Optional.of(movimiento));
        when(movimientoMapper.toDTO(any(Movimientos.class))).thenReturn(movimientoDTO);

        // Act
        Optional<MovimientoDTO> result = movimientosService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Deposito", result.get().getTipoMovimiento());
        assertEquals(new BigDecimal("500.00"), result.get().getValor());
        verify(movimientosRepository, times(1)).findByIdAndActive(1L);
    }

    @Test
    void testFindById_WhenMovimientoNotExists_ShouldReturnEmpty() {
        // Arrange
        when(movimientosRepository.findByIdAndActive(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<MovimientoDTO> result = movimientosService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(movimientosRepository, times(1)).findByIdAndActive(999L);
    }

    @Test
    void testFindByCuentaId_ShouldReturnListOfMovimientos() {
        // Arrange
        List<Movimientos> movimientos = Arrays.asList(movimiento);
        when(movimientosRepository.findByCuentaIdAndActiveOrderByFechaDesc(1L)).thenReturn(movimientos);
        when(movimientoMapper.toDTO(any(Movimientos.class))).thenReturn(movimientoDTO);

        // Act
        List<MovimientoDTO> result = movimientosService.findByCuentaId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(movimientosRepository, times(1)).findByCuentaIdAndActiveOrderByFechaDesc(1L);
    }

    @Test
    void testSave_Deposito_ShouldSaveAndReturnMovimiento() {
        // Arrange
        MovimientoDTO depositoDTO = MovimientoDTO.builder()
                .tipoMovimiento("Deposito")
                .valor(new BigDecimal("500.00"))
                .cuentaId(1L)
                .build();

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientosRepository.findByCuentaIdAndActiveOrderByFechaDesc(1L)).thenReturn(Arrays.asList());
        when(movimientoMapper.toEntity(any(MovimientoDTO.class))).thenReturn(movimiento);
        when(movimientosRepository.save(any(Movimientos.class))).thenReturn(movimiento);
        when(movimientoMapper.toDTO(any(Movimientos.class))).thenReturn(depositoDTO);

        // Act
        MovimientoDTO result = movimientosService.save(depositoDTO);

        // Assert
        assertNotNull(result);
        verify(cuentaRepository, times(2)).findById(1L); // Una para save() y otra para obtenerUltimoSaldo()
        verify(movimientosRepository, times(1)).save(any(Movimientos.class));
    }

    @Test
    void testSave_Retiro_ShouldApplyComisionAndSave() {
        // Arrange
        MovimientoDTO retiroDTO = MovimientoDTO.builder()
                .tipoMovimiento("Retiro")
                .valor(new BigDecimal("200.00"))
                .cuentaId(1L)
                .build();

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientosRepository.findByCuentaIdAndActiveOrderByFechaDesc(1L)).thenReturn(Arrays.asList());
        when(strategyFactory.getStrategy("Ahorros")).thenReturn(cuentaStrategy);
        when(cuentaStrategy.aplicarComision(any(BigDecimal.class))).thenReturn(new BigDecimal("201.00"));
        when(movimientoMapper.toEntity(any(MovimientoDTO.class))).thenReturn(movimiento);
        when(movimientosRepository.save(any(Movimientos.class))).thenReturn(movimiento);
        when(movimientoMapper.toDTO(any(Movimientos.class))).thenReturn(retiroDTO);

        // Act
        MovimientoDTO result = movimientosService.save(retiroDTO);

        // Assert
        assertNotNull(result);
        verify(strategyFactory, times(1)).getStrategy("Ahorros");
        verify(cuentaStrategy, times(1)).aplicarComision(any(BigDecimal.class));
        verify(movimientosRepository, times(1)).save(any(Movimientos.class));
    }

    @Test
    void testSave_InsufficientBalance_ShouldThrowException() {
        // Arrange
        MovimientoDTO retiroDTO = MovimientoDTO.builder()
                .tipoMovimiento("Retiro")
                .valor(new BigDecimal("2000.00")) // Monto mayor al saldo
                .cuentaId(1L)
                .build();

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientosRepository.findByCuentaIdAndActiveOrderByFechaDesc(1L)).thenReturn(Arrays.asList());
        when(strategyFactory.getStrategy("Ahorros")).thenReturn(cuentaStrategy);
        when(cuentaStrategy.aplicarComision(any(BigDecimal.class))).thenReturn(new BigDecimal("2001.00"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientosService.save(retiroDTO);
        });

        assertTrue(exception.getMessage().contains("Saldo insuficiente"));
        verify(movimientosRepository, never()).save(any(Movimientos.class));
    }

    @Test
    void testSave_CuentaNotFound_ShouldThrowException() {
        // Arrange
        MovimientoDTO movimientoDTO = MovimientoDTO.builder()
                .tipoMovimiento("Deposito")
                .valor(new BigDecimal("500.00"))
                .cuentaId(999L) // Cuenta inexistente
                .build();

        when(cuentaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientosService.save(movimientoDTO);
        });

        assertTrue(exception.getMessage().contains("Cuenta no encontrada"));
        verify(movimientosRepository, never()).save(any(Movimientos.class));
    }
}
