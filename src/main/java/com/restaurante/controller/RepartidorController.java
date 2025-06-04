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

import com.restaurante.dto.RepartidorDTO;
import com.restaurante.model.Repartidor;
import com.restaurante.service.IRepartidorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/repartidores")
@SecurityRequirement(name = "bearer-key")
public class RepartidorController {

    private static final Logger logger = LoggerFactory.getLogger(RepartidorController.class);
    private final IRepartidorService repartidorService;

    public RepartidorController(IRepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }

    @Operation(
        summary = "Crear nuevo repartidor",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "201", description = "Repartidor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RepartidorDTO> crearRepartidor(@Valid @RequestBody RepartidorDTO dto) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud POST /api/repartidores");
        
        ResponseEntity<RepartidorDTO> response = new ResponseEntity<>(
            repartidorService.crearRepartidor(dto), 
            HttpStatus.CREATED
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud POST /api/repartidores completada en {} ms. ID creado: {}", 
                   duration, response.getBody().getId());
        return response;
    }

    @Operation(
        summary = "Actualizar repartidor existente",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del repartidor a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Repartidor actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Repartidor no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RepartidorDTO> actualizarRepartidor(
            @PathVariable Long id, @Valid @RequestBody RepartidorDTO dto) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/repartidores/{}", id);
        
        ResponseEntity<RepartidorDTO> response = ResponseEntity.ok(
            repartidorService.actualizarRepartidor(id, dto)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/repartidores/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Listar todos los repartidores",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de repartidores obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RepartidorDTO>> listarTodosRepartidores() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/repartidores");
        
        ResponseEntity<List<RepartidorDTO>> response = ResponseEntity.ok(
            repartidorService.listarTodosRepartidores()
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/repartidores completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener repartidor por ID",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del repartidor", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Repartidor encontrado"),
            @ApiResponse(responseCode = "404", description = "Repartidor no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RepartidorDTO> obtenerRepartidorPorId(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/repartidores/{}", id);
        
        ResponseEntity<RepartidorDTO> response = ResponseEntity.ok(
            repartidorService.obtenerRepartidorPorId(id)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/repartidores/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Eliminar repartidor",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del repartidor a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Repartidor eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Repartidor no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarRepartidor(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud DELETE /api/repartidores/{}", id);
        
        repartidorService.eliminarRepartidor(id);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud DELETE /api/repartidores/{} completada en {} ms", id, duration);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener repartidor con bloqueo optimista",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del repartidor", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Repartidor encontrado"),
            @ApiResponse(responseCode = "404", description = "Repartidor no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Repartidor> obtenerPorIdConBloqueo(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/repartidores/{}/lock", id);
        
        ResponseEntity<Repartidor> response = ResponseEntity.ok(
            repartidorService.obtenerPorIdConBloqueo(id)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/repartidores/{}/lock completada en {} ms", id, duration);
        return response;
    }
}