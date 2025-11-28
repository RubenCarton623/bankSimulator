package com.rvera.sofka.banksimulator.controller;

import com.rvera.sofka.banksimulator.dto.ClienteDTO;
import com.rvera.sofka.banksimulator.service.IClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private IClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
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
    void testGetAllClientes_ShouldReturnListOfClientes() {
        // Arrange
        List<ClienteDTO> clientes = Arrays.asList(clienteDTO);
        when(clienteService.findAll()).thenReturn(clientes);

        // Act
        ResponseEntity<List<ClienteDTO>> response = clienteController.getAllClientes();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Juan Pérez", response.getBody().get(0).getNombre());
        verify(clienteService, times(1)).findAll();
    }

    @Test
    void testGetClienteById_WhenExists_ShouldReturnCliente() {
        // Arrange
        when(clienteService.findById(1L)).thenReturn(Optional.of(clienteDTO));

        // Act
        ResponseEntity<ClienteDTO> response = clienteController.getClienteById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan Pérez", response.getBody().getNombre());
        assertEquals("1234567890", response.getBody().getIdentificacion());
        verify(clienteService, times(1)).findById(1L);
    }

    @Test
    void testGetClienteById_WhenNotExists_ShouldReturn404() {
        // Arrange
        when(clienteService.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ClienteDTO> response = clienteController.getClienteById(999L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).findById(999L);
    }

    @Test
    void testGetClienteByIdentificacion_WhenExists_ShouldReturnCliente() {
        // Arrange
        when(clienteService.findByIdentificacion("1234567890")).thenReturn(Optional.of(clienteDTO));

        // Act
        ResponseEntity<ClienteDTO> response = clienteController.getClienteByIdentificacion("1234567890");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan Pérez", response.getBody().getNombre());
        verify(clienteService, times(1)).findByIdentificacion("1234567890");
    }

    @Test
    void testCreateCliente_WithValidData_ShouldReturnCreated() {
        // Arrange
        ClienteDTO newCliente = ClienteDTO.builder()
                .nombre("María García")
                .genero("F")
                .edad(25)
                .identificacion("9876543210")
                .direccion("Calle 456")
                .telefono("3009876543")
                .contrasena("password456")
                .estado(true)
                .build();

        when(clienteService.save(any(ClienteDTO.class))).thenReturn(newCliente);

        // Act
        ResponseEntity<ClienteDTO> response = clienteController.createCliente(newCliente);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("María García", response.getBody().getNombre());
        verify(clienteService, times(1)).save(any(ClienteDTO.class));
    }

    @Test
    void testUpdateCliente_WhenExists_ShouldReturnUpdatedCliente() {
        // Arrange
        ClienteDTO updatedCliente = ClienteDTO.builder()
                .clienteId(1L)
                .nombre("Juan Pérez Updated")
                .genero("M")
                .edad(31)
                .identificacion("1234567890")
                .direccion("Calle 456")
                .telefono("3001234567")
                .contrasena("newpassword")
                .estado(true)
                .build();

        when(clienteService.findById(1L)).thenReturn(Optional.of(clienteDTO));
        when(clienteService.save(any(ClienteDTO.class))).thenReturn(updatedCliente);

        // Act
        ResponseEntity<ClienteDTO> response = clienteController.updateCliente(1L, updatedCliente);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan Pérez Updated", response.getBody().getNombre());
        verify(clienteService, times(1)).findById(1L);
        verify(clienteService, times(1)).save(any(ClienteDTO.class));
    }

    @Test
    void testUpdateCliente_WhenNotExists_ShouldReturn404() {
        // Arrange
        when(clienteService.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ClienteDTO> response = clienteController.updateCliente(999L, clienteDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clienteService, times(1)).findById(999L);
        verify(clienteService, never()).save(any(ClienteDTO.class));
    }

    @Test
    void testDeleteCliente_WhenExists_ShouldReturnNoContent() {
        // Arrange
        when(clienteService.findById(1L)).thenReturn(Optional.of(clienteDTO));
        doNothing().when(clienteService).deleteById(1L);

        // Act
        ResponseEntity<Void> response = clienteController.deleteCliente(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).findById(1L);
        verify(clienteService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCliente_WhenNotExists_ShouldReturn404() {
        // Arrange
        when(clienteService.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = clienteController.deleteCliente(999L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clienteService, times(1)).findById(999L);
        verify(clienteService, never()).deleteById(anyLong());
    }
}
