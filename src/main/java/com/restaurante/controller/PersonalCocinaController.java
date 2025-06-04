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

import com.restaurante.dto.PersonalCocinaDTO;
import com.restaurante.model.PersonalCocina;
import com.restaurante.service.IPersonalCocinaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/personal-cocina")
@SecurityRequirement(name = "bearer-key")
public class PersonalCocinaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonalCocinaController.class);
    
    private final IPersonalCocinaService personalService;

    public PersonalCocinaController(IPersonalCocinaService personalService) {
        this.personalService = personalService;
    }

    @Operation(
        summary = "Crear nuevo personal de cocina",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "201", description = "Personal creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonalCocinaDTO> crearPersonal(
            @Valid @RequestBody PersonalCocinaDTO dto) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud POST /api/personal-cocina");
        
        ResponseEntity<PersonalCocinaDTO> response = new ResponseEntity<>(
            personalService.crearPersonal(dto), 
            HttpStatus.CREATED
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud POST /api/personal-cocina completada en {} ms. ID creado: {}", 
                   duration, response.getBody().getId());
        return response;
    }

    @Operation(
        summary = "Actualizar personal de cocina",
        description = "Accesible por ADMIN (todos) o PERSONAL_COCINA (solo su propio perfil)",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del personal a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Personal actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Personal no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA') and (#id == principal.username or hasRole('ADMIN'))")
    public ResponseEntity<PersonalCocinaDTO> actualizarPersonal(
            @PathVariable Long id, 
            @Valid @RequestBody PersonalCocinaDTO dto) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/personal-cocina/{}", id);
        
        ResponseEntity<PersonalCocinaDTO> response = ResponseEntity.ok(
            personalService.actualizarPersonal(id, dto)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/personal-cocina/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Listar todo el personal de cocina",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de personal encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonalCocinaDTO>> listarTodoPersonal() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/personal-cocina");
        
        ResponseEntity<List<PersonalCocinaDTO>> response = ResponseEntity.ok(
            personalService.listarTodoPersonal()
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/personal-cocina completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener personal por ID",
        description = "Accesible por ADMIN (todos) o PERSONAL_COCINA (solo su propio perfil)",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del personal", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Personal encontrado"),
            @ApiResponse(responseCode = "404", description = "Personal no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA') and (#id == principal.username or hasRole('ADMIN'))")
    public ResponseEntity<PersonalCocinaDTO> obtenerPersonalPorId(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/personal-cocina/{}", id);
        
        ResponseEntity<PersonalCocinaDTO> response = ResponseEntity.ok(
            personalService.obtenerPersonalPorId(id)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/personal-cocina/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Eliminar personal de cocina",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del personal a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Personal eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Personal no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarPersonal(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud DELETE /api/personal-cocina/{}", id);
        
        personalService.eliminarPersonal(id);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud DELETE /api/personal-cocina/{} completada en {} ms", id, duration);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener personal con bloqueo",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del personal", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Personal encontrado"),
            @ApiResponse(responseCode = "404", description = "Personal no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonalCocina> obtenerPorIdConBloqueo(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/personal-cocina/{}/lock", id);
        
        ResponseEntity<PersonalCocina> response = ResponseEntity.ok(
            personalService.obtenerPorIdConBloqueo(id)
        );
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/personal-cocina/{}/lock completada en {} ms", id, duration);
        return response;
    }
}