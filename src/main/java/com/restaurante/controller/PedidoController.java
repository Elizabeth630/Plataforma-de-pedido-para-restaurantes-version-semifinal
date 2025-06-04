package com.restaurante.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.restaurante.dto.PedidoDTO;
import com.restaurante.model.Pedido;
import com.restaurante.service.IPedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/pedidos")
@SecurityRequirement(name = "bearer-key")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    
    @Autowired
    private IPedidoService pedidoService;

    @Operation(
        summary = "Obtener todos los pedidos",
        description = "Solo accesible por ADMIN y PERSONAL_COCINA",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<PedidoDTO>> obtenerTodos() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/pedidos");
        
        ResponseEntity<List<PedidoDTO>> response = new ResponseEntity<>(
            pedidoService.obtenerTodos(), 
            HttpStatus.OK
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/pedidos completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener pedido por ID",
        description = "Accesible por ADMIN, PERSONAL_COCINA y CLIENTE (solo su propio pedido)",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del pedido", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA', 'CLIENTE')")
    public ResponseEntity<PedidoDTO> obtenerPorId(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/pedidos/{}", id);
        
        ResponseEntity<PedidoDTO> response = new ResponseEntity<>(
            pedidoService.obtenerPorId(id), 
            HttpStatus.OK
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/pedidos/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Obtener pedidos por ID de cliente",
        description = "Accesible por ADMIN y CLIENTE (solo sus propios pedidos)",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idCliente", description = "ID del cliente", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') and (#idCliente == principal.id or hasRole('ADMIN'))")
    public ResponseEntity<List<PedidoDTO>> obtenerPorCliente(@PathVariable Long idCliente) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/pedidos/cliente/{}", idCliente);
        
        ResponseEntity<List<PedidoDTO>> response = new ResponseEntity<>(
            pedidoService.obtenerPorCliente(idCliente), 
            HttpStatus.OK
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/pedidos/cliente/{} completada en {} ms", idCliente, duration);
        return response;
    }

    @Operation(
        summary = "Obtener pedidos por estado",
        description = "Solo accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "estado", description = "Estado del pedido", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<PedidoDTO>> obtenerPorEstado(@PathVariable String estado) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/pedidos/estado/{}", estado);
        
        ResponseEntity<List<PedidoDTO>> response = new ResponseEntity<>(
            pedidoService.obtenerPorEstado(estado), 
            HttpStatus.OK
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/pedidos/estado/{} completada en {} ms", estado, duration);
        return response;
    }

    @Operation(
        summary = "Obtener pedidos del día actual",
        description = "Solo accesible por ADMIN y PERSONAL_COCINA",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/hoy")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosDelDia() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/pedidos/hoy");
        
        ResponseEntity<List<PedidoDTO>> response = new ResponseEntity<>(
            pedidoService.obtenerPedidosDelDia(), 
            HttpStatus.OK
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/pedidos/hoy completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Crear nuevo pedido",
        description = "Accesible por ADMIN y CLIENTE",
        responses = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<PedidoDTO> crear(@RequestBody PedidoDTO pedidoDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud POST /api/pedidos");
        
        ResponseEntity<PedidoDTO> response = new ResponseEntity<>(
            pedidoService.crear(pedidoDTO), 
            HttpStatus.CREATED
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud POST /api/pedidos completada en {} ms. ID creado: {}", 
                   duration, response.getBody().getPid());
        return response;
    }

    @Operation(
        summary = "Actualizar pedido existente",
        description = "Solo accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del pedido a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<PedidoDTO> actualizar(
            @PathVariable Long id, 
            @RequestBody PedidoDTO pedidoDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/pedidos/{}", id);
        
        ResponseEntity<PedidoDTO> response = new ResponseEntity<>(
            pedidoService.actualizar(id, pedidoDTO), 
            HttpStatus.OK
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/pedidos/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Actualizar estado de pedido",
        description = "Solo accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del pedido", required = true),
            @Parameter(in = ParameterIn.PATH, name = "nuevoEstado", description = "Nuevo estado del pedido", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PutMapping("/{id}/estado/{nuevoEstado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<PedidoDTO> actualizarEstado(
            @PathVariable Long id,
            @PathVariable String nuevoEstado) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/pedidos/{}/estado/{}", id, nuevoEstado);
        
        ResponseEntity<PedidoDTO> response = new ResponseEntity<>(
            pedidoService.actualizarEstado(id, nuevoEstado), 
            HttpStatus.OK
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/pedidos/{}/estado/{} completada en {} ms", 
                   id, nuevoEstado, duration);
        return response;
    }

    @Operation(
        summary = "Eliminar pedido",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del pedido a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud DELETE /api/pedidos/{}", id);
        
        pedidoService.eliminar(id);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud DELETE /api/pedidos/{} completada en {} ms", id, duration);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @Operation(
        summary = "Obtener pedido con bloqueo",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del pedido", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pedido> obtenerPorIdConBloqueo(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/pedidos/{}/lock", id);
        
        ResponseEntity<Pedido> response = ResponseEntity.ok(
            pedidoService.obtenerPorIdConBloqueo(id)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/pedidos/{}/lock completada en {} ms", id, duration);
        return response;
    }
}