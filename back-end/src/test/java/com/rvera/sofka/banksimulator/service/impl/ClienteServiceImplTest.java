package com.rvera.sofka.banksimulator.service.impl;

import com.rvera.sofka.banksimulator.dto.ClienteDTO;
import com.rvera.sofka.banksimulator.entity.Cliente;
import com.rvera.sofka.banksimulator.entity.Persona;
import com.rvera.sofka.banksimulator.mapper.ClienteMapper;
import com.rvera.sofka.banksimulator.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private Persona persona;

    @BeforeEach
    void setUp() {
        // Setup Persona
        persona = new Persona();
        persona.setId(1L);
        persona.setNombre("Juan Pérez");
        persona.setGenero("M");
        persona.setEdad(30);
        persona.setIdentificacion("1234567890");
        persona.setDireccion("Calle 123");
        persona.setTelefono("3001234567");

        // Setup Cliente entity
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setPersona(persona);
        cliente.setContrasena("password123");
        cliente.setEstado(true);

        // Setup ClienteDTO
        clienteDTO = ClienteDTO.builder()
                .clienteId(1L)
                .nombre("Juan Pérez")
                .genero("M")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Calle 123")
                .telefono("3001234567")
                .contrasena("password123")
                .estado(true)
                .build();
    }

    @Test
    void testFindAll_ShouldReturnListOfClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAllActive()).thenReturn(clientes);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // Act
        List<ClienteDTO> result = clienteService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clienteRepository, times(1)).findAllActive();
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }

    @Test
    void testFindById_WhenClienteExists_ShouldReturnCliente() {
        // Arrange
        when(clienteRepository.findByIdAndActive(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // Act
        Optional<ClienteDTO> result = clienteService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Pérez", result.get().getNombre());
        assertEquals("1234567890", result.get().getIdentificacion());
        verify(clienteRepository, times(1)).findByIdAndActive(1L);
    }

    @Test
    void testFindById_WhenClienteNotExists_ShouldReturnEmpty() {
        // Arrange
        when(clienteRepository.findByIdAndActive(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<ClienteDTO> result = clienteService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(clienteRepository, times(1)).findByIdAndActive(999L);
    }

    @Test
    void testFindByIdentificacion_WhenClienteExists_ShouldReturnCliente() {
        // Arrange
        when(clienteRepository.findByPersonaIdentificacionAndActive("1234567890")).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // Act
        Optional<ClienteDTO> result = clienteService.findByIdentificacion("1234567890");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1234567890", result.get().getIdentificacion());
        verify(clienteRepository, times(1)).findByPersonaIdentificacionAndActive("1234567890");
    }

    @Test
    void testSave_NewCliente_ShouldSaveAndReturnCliente() {
        // Arrange
        ClienteDTO newClienteDTO = ClienteDTO.builder()
                .nombre("María García")
                .genero("F")
                .edad(25)
                .identificacion("9876543210")
                .direccion("Calle 456")
                .telefono("3009876543")
                .contrasena("password456")
                .estado(true)
                .build();

        when(clienteRepository.findByPersonaIdentificacion("9876543210")).thenReturn(Optional.empty());
        when(clienteMapper.toEntity(any(ClienteDTO.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(newClienteDTO);

        // Act
        ClienteDTO result = clienteService.save(newClienteDTO);

        // Assert
        assertNotNull(result);
        verify(clienteRepository, times(1)).findByPersonaIdentificacion("9876543210");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testSave_DuplicateIdentificacion_ShouldThrowException() {
        // Arrange
        ClienteDTO newClienteDTO = ClienteDTO.builder()
                .nombre("María García")
                .identificacion("1234567890") // Identificación duplicada
                .build();

        when(clienteRepository.findByPersonaIdentificacion("1234567890")).thenReturn(Optional.of(cliente));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.save(newClienteDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un cliente con la identificación"));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testSave_UpdateExistingCliente_ShouldUpdateAndReturn() {
        // Arrange
        clienteDTO.setClienteId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // Act
        ClienteDTO result = clienteService.save(clienteDTO);

        // Assert
        assertNotNull(result);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteMapper, times(1)).updateEntityFromDTO(any(ClienteDTO.class), any(Cliente.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testSave_UpdateNonExistingCliente_ShouldThrowException() {
        // Arrange
        clienteDTO.setClienteId(999L);
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.save(clienteDTO);
        });

        assertTrue(exception.getMessage().contains("Cliente no encontrado"));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testDeleteById_ShouldCallRepository() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        // Act
        clienteService.deleteById(1L);

        // Assert
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
}
