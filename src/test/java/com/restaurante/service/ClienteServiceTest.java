package com.restaurante.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.restaurante.dto.ClienteDTO;
import com.restaurante.model.Cliente;
import com.restaurante.repository.ClienteRepository;
import com.restaurante.service.impl.ClienteServiceImpl;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void crearCliente_devuelveClienteDTO() {
        // Arrange
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan");
        clienteDTO.setEmail("juan@example.com");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre("Juan");
        clienteGuardado.setEmail("juan@example.com");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        // Act
        ClienteDTO resultado = clienteService.crearCliente(clienteDTO);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void actualizarCliente_devuelveClienteActualizado() {
        // Arrange
        Long id = 1L;
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan Actualizado");
        clienteDTO.setEmail("juan.actualizado@example.com");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNombre("Juan");
        clienteExistente.setEmail("juan@example.com");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ClienteDTO resultado = clienteService.actualizarCliente(id, clienteDTO);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getNombre()).isEqualTo("Juan Actualizado");
        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void listarTodosClientes_devuelveListaClientes() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNombre("Juan");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNombre("Maria");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        // Act
        List<ClienteDTO> resultado = clienteService.listarTodosClientes();

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Maria");
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void obtenerClientePorId_devuelveClienteDTO() {
        // Arrange
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Juan");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // Act
        ClienteDTO resultado = clienteService.obtenerClientePorId(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void eliminarCliente_llamaMetodoDelete() {
        // Arrange
        Long id = 1L;
        doNothing().when(clienteRepository).deleteById(id);

        // Act
        clienteService.eliminarCliente(id);

        // Assert
        verify(clienteRepository, times(1)).deleteById(id);
        verify(clienteRepository, never()).existsById(any()); // Opcional: verificar que NO se llame
    }

    @Test
    void obtenerClientePorIdConBloqueo_devuelveCliente() {
        // Arrange
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Juan");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.obtenerClientePorIdConBloqueo(id);

        // Assert
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        verify(clienteRepository, times(1)).findById(id);
    }
}