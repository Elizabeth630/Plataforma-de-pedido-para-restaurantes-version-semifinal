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

import com.restaurante.dto.AsignacionRepartidorDTO;
import com.restaurante.model.AsignacionRepartidor;
import com.restaurante.service.IAsignacionRepartidorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/asignaciones-repartidores")
@SecurityRequirement(name = "bearer-key") // Seguridad global para todos los endpoints
public class AsignacionRepartidorController {

    @Autowired
    private IAsignacionRepartidorService asignacionService;

    @Operation(
        summary = "Obtener todas las asignaciones",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de asignaciones encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AsignacionRepartidorDTO>> obtenerTodas() {
        return new ResponseEntity<>(asignacionService.obtenerTodas(), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener asignación por ID",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la asignación", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Asignación encontrada"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<AsignacionRepartidorDTO> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(asignacionService.obtenerPorId(id), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener asignación por ID de pedido",
        description = "Accesible por ADMIN, PERSONAL_COCINA y CLIENTE",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idPedido", description = "ID del pedido asociado", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Asignación encontrada"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/pedido/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA', 'CLIENTE')")
    public ResponseEntity<AsignacionRepartidorDTO> obtenerPorPedido(@PathVariable Long idPedido) {
        return new ResponseEntity<>(asignacionService.obtenerPorPedido(idPedido), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener asignaciones por ID de repartidor",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idRepartidor", description = "ID del repartidor", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de asignaciones encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/repartidor/{idRepartidor}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AsignacionRepartidorDTO>> obtenerPorRepartidor(@PathVariable Long idRepartidor) {
        return new ResponseEntity<>(asignacionService.obtenerPorRepartidor(idRepartidor), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener asignaciones pendientes",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de asignaciones pendientes"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<AsignacionRepartidorDTO>> obtenerAsignacionesPendientes() {
        return new ResponseEntity<>(asignacionService.obtenerAsignacionesPendientes(), HttpStatus.OK);
    }

    @Operation(
        summary = "Crear nueva asignación",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "201", description = "Asignación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AsignacionRepartidorDTO> crear(@RequestBody AsignacionRepartidorDTO asignacionDTO) {
        return new ResponseEntity<>(asignacionService.crear(asignacionDTO), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Actualizar asignación existente",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la asignación a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Asignación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AsignacionRepartidorDTO> actualizar(
            @PathVariable Long id,
            @RequestBody AsignacionRepartidorDTO asignacionDTO) {
        return new ResponseEntity<>(asignacionService.actualizar(id, asignacionDTO), HttpStatus.OK);
    }

    @Operation(
        summary = "Registrar entrega de asignación",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la asignación a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Entrega registrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PutMapping("/{id}/registrar-entrega")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<AsignacionRepartidorDTO> registrarEntrega(@PathVariable Long id) {
        return new ResponseEntity<>(asignacionService.registrarEntrega(id), HttpStatus.OK);
    }

    @Operation(
        summary = "Eliminar asignación",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la asignación a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Asignación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asignacionService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
        summary = "Obtener asignación con bloqueo",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la asignación", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Asignación encontrada"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AsignacionRepartidor> obtenerAsignacionConBloqueo(@PathVariable Long id) {
        AsignacionRepartidor asignacion = asignacionService.obtenerPorIdConBloqueo(id);
        return ResponseEntity.ok(asignacion);
    }
}