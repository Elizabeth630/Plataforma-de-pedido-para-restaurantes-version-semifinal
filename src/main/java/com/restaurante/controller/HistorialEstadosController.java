package com.restaurante.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.dto.HistorialEstadosDTO;
import com.restaurante.model.HistorialEstados;
import com.restaurante.service.IHistorialEstadosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/historial-estados")
@SecurityRequirement(name = "bearer-key")
public class HistorialEstadosController {

    @Autowired
    private IHistorialEstadosService historialService;

    @Operation(
        summary = "Obtener todo el historial de estados",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista completa del historial"),
            @ApiResponse(responseCode = "204", description = "Historial vacío", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistorialEstadosDTO>> obtenerTodos() {
        return new ResponseEntity<>(historialService.obtenerTodos(), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener entrada del historial por ID",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la entrada en el historial", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Entrada del historial encontrada"),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<HistorialEstadosDTO> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(historialService.obtenerPorId(id), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener historial de estados por ID de pedido",
        description = "Accesible por ADMIN, PERSONAL_COCINA y CLIENTE",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idPedido", description = "ID del pedido asociado", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Historial del pedido obtenido"),
            @ApiResponse(responseCode = "204", description = "Pedido sin historial", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/pedido/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA', 'CLIENTE')")
    public ResponseEntity<List<HistorialEstadosDTO>> obtenerPorPedido(@PathVariable Long idPedido) {
        return new ResponseEntity<>(historialService.obtenerPorPedido(idPedido), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener historial por estado específico",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "estado", description = "Nombre del estado a buscar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Historial filtrado por estado"),
            @ApiResponse(responseCode = "204", description = "No hay entradas con este estado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<HistorialEstadosDTO>> obtenerPorEstado(@PathVariable String estado) {
        return new ResponseEntity<>(historialService.obtenerPorEstado(estado), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener historial por ID de cliente",
        description = "Accesible por ADMIN o por el propio CLIENTE (dueño del recurso)",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idCliente", description = "ID del cliente asociado", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Historial del cliente obtenido"),
            @ApiResponse(responseCode = "204", description = "Cliente sin historial", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') and (#idCliente == principal.id or hasRole('ADMIN'))")
    public ResponseEntity<List<HistorialEstadosDTO>> obtenerPorCliente(@PathVariable Long idCliente) {
        return new ResponseEntity<>(historialService.obtenerPorCliente(idCliente), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener último estado de un pedido",
        description = "Accesible por ADMIN, PERSONAL_COCINA y CLIENTE",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idPedido", description = "ID del pedido", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Último estado del pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido sin historial o no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/pedido/{idPedido}/ultimo")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA', 'CLIENTE')")
    public ResponseEntity<HistorialEstadosDTO> obtenerUltimoEstado(@PathVariable Long idPedido) {
        return new ResponseEntity<>(historialService.obtenerUltimoEstado(idPedido), HttpStatus.OK);
    }

    @Operation(
        summary = "Crear nueva entrada en el historial",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        responses = {
            @ApiResponse(responseCode = "201", description = "Entrada creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido asociado no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<HistorialEstadosDTO> crear(@RequestBody HistorialEstadosDTO historialDTO) {
        return new ResponseEntity<>(historialService.crear(historialDTO), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Eliminar entrada del historial",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la entrada a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Entrada eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historialService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
        summary = "Obtener entrada del historial con bloqueo de escritura",
        description = "Solo accesible por ADMIN. Bloquea el registro para operaciones concurrentes",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la entrada en el historial", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Entrada encontrada"),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HistorialEstados> obtenerPorIdConBloqueo(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.obtenerPorIdConBloqueo(id));
    }
}