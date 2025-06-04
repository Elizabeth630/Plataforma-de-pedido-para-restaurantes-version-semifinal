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

import com.restaurante.dto.DetallePedidoDTO;
import com.restaurante.service.IDetallePedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/detalles-pedido")
@SecurityRequirement(name = "bearer-key")
public class DetallePedidoController {

    @Autowired
    private IDetallePedidoService detalleService;

    @Operation(
        summary = "Obtener todos los detalles de pedidos",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de detalles obtenida"),
            @ApiResponse(responseCode = "204", description = "No hay detalles registrados", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<DetallePedidoDTO>> obtenerTodos() {
        return ResponseEntity.ok(detalleService.obtenerTodos());
    }

    @Operation(
        summary = "Obtener detalle por ID",
        description = "Accesible por ADMIN, PERSONAL_COCINA y CLIENTE",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del detalle", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Detalle encontrado"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA', 'CLIENTE')")
    public ResponseEntity<DetallePedidoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(detalleService.obtenerPorId(id));
    }

    @Operation(
        summary = "Obtener detalles por ID de pedido",
        description = "Accesible por ADMIN, PERSONAL_COCINA y CLIENTE",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idPedido", description = "ID del pedido asociado", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de detalles del pedido"),
            @ApiResponse(responseCode = "204", description = "Pedido sin detalles", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/pedido/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA', 'CLIENTE')")
    public ResponseEntity<List<DetallePedidoDTO>> obtenerPorPedido(@PathVariable Long idPedido) {
        return ResponseEntity.ok(detalleService.obtenerPorPedido(idPedido));
    }

    @Operation(
        summary = "Obtener detalles por ID de producto",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idProducto", description = "ID del producto asociado", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de detalles del producto"),
            @ApiResponse(responseCode = "204", description = "Producto sin detalles", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/producto/{idProducto}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<DetallePedidoDTO>> obtenerPorProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(detalleService.obtenerPorProducto(idProducto));
    }

    @Operation(
        summary = "Obtener detalles con instrucciones especiales",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de detalles con instrucciones especiales"),
            @ApiResponse(responseCode = "204", description = "No hay detalles con instrucciones especiales", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @GetMapping("/instrucciones-especiales")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<List<DetallePedidoDTO>> obtenerConInstruccionesEspeciales() {
        return ResponseEntity.ok(detalleService.obtenerConInstruccionesEspeciales());
    }

    @Operation(
        summary = "Crear nuevo detalle de pedido",
        description = "Accesible por ADMIN y CLIENTE",
        responses = {
            @ApiResponse(responseCode = "201", description = "Detalle creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido o producto asociado no encontrado", content = @Content)
        }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<DetallePedidoDTO> crear(@RequestBody DetallePedidoDTO detalleDTO) {
        return new ResponseEntity<>(detalleService.crear(detalleDTO), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Actualizar detalle existente",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del detalle a actualizar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Detalle actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<DetallePedidoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody DetallePedidoDTO detalleDTO) {
        return ResponseEntity.ok(detalleService.actualizar(id, detalleDTO));
    }

    @Operation(
        summary = "Eliminar detalle por ID",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "ID del detalle a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detalleService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Eliminar todos los detalles de un pedido",
        description = "Accesible por ADMIN y PERSONAL_COCINA",
        parameters = {
            @Parameter(in = ParameterIn.PATH, name = "idPedido", description = "ID del pedido asociado", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Detalles eliminados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
        }
    )
    @DeleteMapping("/pedido/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_COCINA')")
    public ResponseEntity<Void> eliminarTodosDePedido(@PathVariable Long idPedido) {
        detalleService.eliminarTodosDePedido(idPedido);
        return ResponseEntity.noContent().build();
    }
}