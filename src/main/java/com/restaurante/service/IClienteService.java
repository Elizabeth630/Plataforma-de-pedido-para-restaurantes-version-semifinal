package com.restaurante.service;
import com.restaurante.dto.ClienteDTO;
import com.restaurante.model.Cliente;

import java.util.List;

public interface IClienteService {
    ClienteDTO crearCliente(ClienteDTO clienteDTO);
    ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO);
    List<ClienteDTO> listarTodosClientes();
    ClienteDTO obtenerClientePorId(Long id);
    void eliminarCliente(Long id);

    Cliente obtenerClientePorIdConBloqueo(Long id);
}