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

import com.restaurante.dto.ProductoDTO;
import com.restaurante.model.Producto;
import com.restaurante.service.IProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/productos")
@SecurityRequirement(name = "bearer-key")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private IProductoService productoService;

    @Operation(
        summary = "Obtener todos los productos",
        description = "Acceso público sin autenticación",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de productos encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/productos");
        
        ResponseEntity<List<ProductoDTO>> response = ResponseEntity.ok(productoService.obtenerTodos());
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/productos completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener productos activos",
        description = "Acceso público sin autenticación",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de productos activos encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/activos")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductoDTO>> obtenerActivos() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/productos/activos");
        
        ResponseEntity<List<ProductoDTO>> response = ResponseEntity.ok(productoService.obtenerActivos());
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/productos/activos completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener productos destacados",
        description = "Acceso público sin autenticación",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de productos destacados encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/destacados")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductoDTO>> obtenerDestacados() {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/productos/destacados");
        
        ResponseEntity<List<ProductoDTO>> response = ResponseEntity.ok(productoService.obtenerDestacados());
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/productos/destacados completada en {} ms", duration);
        return response;
    }

    @Operation(
        summary = "Obtener productos por categoría",
        description = "Acceso público sin autenticación",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idCategoria", description = "ID de la categoría", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de productos encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/categoria/{idCategoria}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductoDTO>> obtenerPorCategoria(@PathVariable Long idCategoria) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/productos/categoria/{}", idCategoria);
        
        ResponseEntity<List<ProductoDTO>> response = ResponseEntity.ok(productoService.obtenerPorCategoria(idCategoria));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/productos/categoria/{} completada en {} ms", idCategoria, duration);
        return response;
    }

    @Operation(
        summary = "Obtener producto por ID",
        description = "Acceso público sin autenticación",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/productos/{}", id);
        
        ResponseEntity<ProductoDTO> response = ResponseEntity.ok(productoService.obtenerPorId(id));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/productos/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Crear nuevo producto",
        description = "Solo accesible por ADMIN",
        responses = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO productoDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud POST /api/productos");
        
        ResponseEntity<ProductoDTO> response = ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.crear(productoDTO));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud POST /api/productos completada en {} ms. ID creado: {}", 
                   duration, response.getBody().getId());
        return response;
    }

    @Operation(
        summary = "Actualizar producto existente",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/productos/{}", id);
        
        ResponseEntity<ProductoDTO> response = ResponseEntity.ok(productoService.actualizar(id, productoDTO));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/productos/{} completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Desactivar producto",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto a desactivar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Producto desactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> desactivar(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud PUT /api/productos/{}/desactivar", id);
        
        ResponseEntity<ProductoDTO> response = ResponseEntity.ok(productoService.desactivar(id));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud PUT /api/productos/{}/desactivar completada en {} ms", id, duration);
        return response;
    }

    @Operation(
        summary = "Buscar productos por nombre",
        description = "Acceso público sin autenticación",
        parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "nombre", description = "Nombre o parte del nombre del producto", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de productos encontrados"),
            @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        },
        security = {}
    )
    @GetMapping("/buscar")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductoDTO>> buscarPorNombre(@RequestParam String nombre) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/productos/buscar?nombre={}", nombre);
        
        ResponseEntity<List<ProductoDTO>> response = ResponseEntity.ok(productoService.buscarPorNombre(nombre));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/productos/buscar completada en {} ms. Parámetro: {}", duration, nombre);
        return response;
    }
    
    @Operation(
        summary = "Eliminar producto permanentemente",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud DELETE /api/productos/{}", id);
        
        productoService.eliminar(id);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud DELETE /api/productos/{} completada en {} ms", id, duration);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Obtener producto con bloqueo optimista",
        description = "Solo accesible por ADMIN",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        }
    )
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> obtenerPorIdConBloqueo(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        logger.info("Iniciando solicitud GET /api/productos/{}/lock", id);
        
        ResponseEntity<Producto> response = ResponseEntity.ok(productoService.obtenerPorIdConBloqueo(id));
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud GET /api/productos/{}/lock completada en {} ms", id, duration);
        return response;
    }
}