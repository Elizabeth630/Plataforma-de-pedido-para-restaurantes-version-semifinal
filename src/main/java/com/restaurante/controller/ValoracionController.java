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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.dto.ValoracionDTO;
import com.restaurante.model.Valoracion;
import com.restaurante.service.IValoracionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/valoraciones")
@SecurityRequirement(name = "bearer-key")
public class ValoracionController {

    @Autowired
    private IValoracionService valoracionService;

    @Operation(
        summary = "Obtener todas las valoraciones",
        description = "Acceso público sin autenticación",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de valoraciones obtenida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ValoracionDTO>> obtenerTodas() {
        return new ResponseEntity<>(valoracionService.obtenerTodas(), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener valoración por ID",
        description = "Acceso público sin autenticación",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la valoración", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Valoración encontrada"),
            @ApiResponse(responseCode = "404", description = "Valoración no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ValoracionDTO> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(valoracionService.obtenerPorId(id), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener valoraciones por ID de pedido",
        description = "Accesible por ADMIN y CLIENTE",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idPedido", description = "ID del pedido asociado", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de valoraciones encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @GetMapping("/pedido/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<List<ValoracionDTO>> obtenerPorPedido(@PathVariable Long idPedido) {
        return new ResponseEntity<>(valoracionService.obtenerPorPedido(idPedido), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener valoraciones por ID de cliente",
        description = "Un cliente solo puede ver sus propias valoraciones. Los ADMIN pueden ver todas.",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idCliente", description = "ID del cliente", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de valoraciones encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - No puedes ver valoraciones de otros clientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') and (#idCliente == principal.id or hasRole('ADMIN'))")
    public ResponseEntity<List<ValoracionDTO>> obtenerPorCliente(@PathVariable Long idCliente) {
        return new ResponseEntity<>(valoracionService.obtenerPorCliente(idCliente), HttpStatus.OK);
    }

    @Operation(
        summary = "Crear nueva valoración",
        description = "Accesible por ADMIN y CLIENTE",
        responses = {
            @ApiResponse(responseCode = "201", description = "Valoración creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<ValoracionDTO> crear(@RequestBody ValoracionDTO valoracionDTO) {
        return new ResponseEntity<>(valoracionService.crear(valoracionDTO), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Actualizar valoración existente",
        description = "Accesible por ADMIN y CLIENTE",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la valoración a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Valoración actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo el propietario o ADMIN puede actualizar", content = @Content),
            @ApiResponse(responseCode = "404", description = "Valoración no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<ValoracionDTO> actualizar(
            @PathVariable Long id, 
            @RequestBody ValoracionDTO valoracionDTO) {
        return new ResponseEntity<>(valoracionService.actualizar(id, valoracionDTO), HttpStatus.OK);
    }

    @Operation(
        summary = "Eliminar valoración",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la valoración a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Valoración eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Valoración no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        valoracionService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
        summary = "Obtener puntuación promedio por pedido",
        description = "Acceso público sin autenticación",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idPedido", description = "ID del pedido", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Puntuación promedio calculada"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/promedio/pedido/{idPedido}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Double> obtenerPromedioPorPedido(@PathVariable Long idPedido) {
        return new ResponseEntity<>(valoracionService.obtenerPuntuacionPromedioPorPedido(idPedido), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener puntuación promedio por cliente",
        description = "Acceso público sin autenticación",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idCliente", description = "ID del cliente", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Puntuación promedio calculada"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/promedio/cliente/{idCliente}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Double> obtenerPromedioPorCliente(@PathVariable Long idCliente) {
        return new ResponseEntity<>(valoracionService.obtenerPuntuacionPromedioPorCliente(idCliente), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener valoración con bloqueo optimista",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la valoración", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Valoración encontrada"),
            @ApiResponse(responseCode = "404", description = "Valoración no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Valoracion> obtenerPorIdConBloqueo(@PathVariable Long id) {
        return ResponseEntity.ok(valoracionService.obtenerPorIdConBloqueo(id));
    }
}