package com.restaurante.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.dto.ClienteDTO;
import com.restaurante.model.Cliente;
import com.restaurante.service.IClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/clientes")
@SecurityRequirement(name = "bearer-key")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    
    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
        summary = "Crear nuevo cliente",
        description = "Accesible públicamente sin autenticación",
        responses = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud POST /api/clientes");
        
        ResponseEntity<ClienteDTO> response = new ResponseEntity<>(
            clienteService.crearCliente(clienteDTO), 
            HttpStatus.CREATED
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud POST /api/clientes completada en {} ms. ID creado: {}", 
                   duration, response.getBody().getId());
        return response;
    }

    @Operation(
        summary = "Actualizar cliente existente",
        description = "Accesible por ADMIN o por el propio CLIENTE (dueño del recurso)",
        parameters = {
            @Parameter(
                in = ParameterIn.PATH, 
                name = "id", 
                description = "ID del cliente a actualizar", 
                required = true
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') and (#id == principal.username or hasRole('ADMIN'))")
    public ResponseEntity<ClienteDTO> actualizarCliente(
            @PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/clientes/{}", id);
        
        ResponseEntity<ClienteDTO> response = ResponseEntity.ok(
            clienteService.actualizarCliente(id, clienteDTO)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/clientes/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Listar todos los clientes",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "204", description = "No hay clientes registrados", content = @Content)
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/clientes");
        
        ResponseEntity<List<ClienteDTO>> response = ResponseEntity.ok(
            clienteService.listarTodosClientes()
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/clientes completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener cliente por ID",
        description = "Accesible por ADMIN o por el propio CLIENTE (dueño del recurso)",
        parameters = {
            @Parameter(
                in = ParameterIn.PATH, 
                name = "id", 
                description = "ID del cliente a consultar", 
                required = true
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') and (#id == principal.username or hasRole('ADMIN'))")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/clientes/{}", id);
        
        ResponseEntity<ClienteDTO> response = ResponseEntity.ok(
            clienteService.obtenerClientePorId(id)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/clientes/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Eliminar cliente",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(
                in = ParameterIn.PATH, 
                name = "id", 
                description = "ID del cliente a eliminar", 
                required = true
            )
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud DELETE /api/clientes/{}", id);
        
        clienteService.eliminarCliente(id);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud DELETE /api/clientes/{} completada en {} ms", id, duration);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener cliente con bloqueo de escritura",
        description = "Solo accesible por ADMIN. Bloquea el registro para operaciones concurrentes",
        parameters = {
            @Parameter(
                in = ParameterIn.PATH, 
                name = "id", 
                description = "ID del cliente a consultar con bloqueo", 
                required = true
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> obtenerClientePorIdConBloqueo(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/clientes/{}/lock", id);
        
        ResponseEntity<Cliente> response = ResponseEntity.ok(
            clienteService.obtenerClientePorIdConBloqueo(id)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/clientes/{}/lock completada en {} ms", id, duration);
        return response;
    }
}