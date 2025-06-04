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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.dto.CategoriaDTO;
import com.restaurante.model.Categoria;
import com.restaurante.service.ICategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private ICategoriaService categoriaService;

    @Operation(
        summary = "Obtener todas las categorías",
        description = "Endpoint público que retorna todas las categorías disponibles",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida con éxito")
        }
    )
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoriaDTO>> obtenerTodas() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/categorias");
        
        ResponseEntity<List<CategoriaDTO>> response = ResponseEntity.ok(categoriaService.obtenerTodas());
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/categorias completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener categorías activas",
        description = "Endpoint público que retorna solo las categorías activas",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías activas obtenida con éxito")
        }
    )
    @GetMapping("/activas")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoriaDTO>> obtenerActivas() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/categorias/activas");
        
        ResponseEntity<List<CategoriaDTO>> response = ResponseEntity.ok(categoriaService.obtenerActivas());
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/categorias/activas completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener categoría por ID",
        description = "Endpoint público que retorna una categoría específica por su ID",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la categoría", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoriaDTO> obtenerPorId(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/categorias/{}", id);
        
        ResponseEntity<CategoriaDTO> response = ResponseEntity.ok(categoriaService.obtenerPorId(id));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/categorias/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Crear nueva categoría",
        description = "Requiere rol ADMIN. Crea una nueva categoría de productos",
        security = @SecurityRequirement(name = "bearer-key"),
        responses = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> crear(@RequestBody CategoriaDTO categoriaDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud POST /api/categorias");
        
        ResponseEntity<CategoriaDTO> response = ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.crear(categoriaDTO));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud POST /api/categorias completada en {} ms. ID creado: {}", 
                   duration, response.getBody().getId());
        return response;
    }

    @Operation(
        summary = "Actualizar categoría",
        description = "Requiere rol ADMIN. Actualiza una categoría existente",
        security = @SecurityRequirement(name = "bearer-key"),
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la categoría a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/categorias/{}", id);
        
        ResponseEntity<CategoriaDTO> response = ResponseEntity.ok(categoriaService.actualizar(id, categoriaDTO));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/categorias/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Desactivar categoría",
        description = "Requiere rol ADMIN. Desactiva una categoría existente",
        security = @SecurityRequirement(name = "bearer-key"),
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la categoría a desactivar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría desactivada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> desactivar(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/categorias/{}/desactivar", id);
        
        ResponseEntity<CategoriaDTO> response = ResponseEntity.ok(categoriaService.desactivar(id));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/categorias/{}/desactivar completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Buscar categorías por nombre",
        description = "Endpoint público que busca categorías por coincidencia de nombre",
        parameters = {
            @Parameter(
                in = ParameterIn.QUERY, 
                name = "nombre", 
                description = "Texto para buscar en nombres de categorías", 
                required = true,
                example = "bebida"
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Resultados de búsqueda")
        }
    )
    @GetMapping("/buscar")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoriaDTO>> buscarPorNombre(@RequestParam String nombre) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/categorias/buscar?nombre={}", nombre);
        
        ResponseEntity<List<CategoriaDTO>> response = ResponseEntity.ok(categoriaService.buscarPorNombre(nombre));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/categorias/buscar completada en {} ms. Parámetro: {}", duration, nombre);
        return response;
    }
    
    @Operation(
        summary = "Eliminar categoría",
        description = "Requiere rol ADMIN. Elimina permanentemente una categoría",
        security = @SecurityRequirement(name = "bearer-key"),
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la categoría a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud DELETE /api/categorias/{}", id);
        
        categoriaService.eliminar(id);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud DELETE /api/categorias/{} completada en {} ms", id, duration);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Obtener categoría con bloqueo",
        description = "Requiere rol ADMIN. Obtiene una categoría con bloqueo optimista",
        security = @SecurityRequirement(name = "bearer-key"),
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la categoría", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida con bloqueo"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categoria> obtenerPorIdConBloqueo(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/categorias/{}/lock", id);
        
        ResponseEntity<Categoria> response = ResponseEntity.ok(categoriaService.obtenerPorIdConBloqueo(id));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/categorias/{}/lock completada en {} ms", id, duration);
        return response;
    }
}