package com.perfulandia.ms_pedidos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perfulandia.ms_pedidos.model.Pedido;
import com.perfulandia.ms_pedidos.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<?> getPedidos() {
        log.info("GET /api/v1/pedidos - Listar todos los pedidos");
        List<Pedido> lista = pedidoService.findAll();
        if (lista.isEmpty()) {
            log.warn("No hay pedidos registrados");
            return ResponseEntity.status(404).body("No hay pedidos registrados");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        log.info("GET /api/v1/pedidos/{} - Buscar pedido por id", id);
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Pedido con id {} no encontrado", id);
                    return ResponseEntity.status(404).build();
                });
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> getPedidosByCliente(@PathVariable Long idCliente) {
        log.info("GET /api/v1/pedidos/cliente/{} - Buscar pedidos por cliente", idCliente);
        List<Pedido> lista = pedidoService.findByIdCliente(idCliente);
        if (lista.isEmpty()) {
            log.warn("No hay pedidos para el cliente {}", idCliente);
            return ResponseEntity.status(404).body("No hay pedidos para ese cliente");
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> postPedido(@Valid @RequestBody Pedido pedido) {
        log.info("POST /api/v1/pedidos - Crear nuevo pedido");
        Pedido nuevo = pedidoService.save(pedido);
        log.info("Pedido creado exitosamente con id: {}", nuevo.getIdPedido());
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> putEstadoPedido(@PathVariable Long id,
            @RequestParam String estado) {
        log.info("PUT /api/v1/pedidos/{}/estado - Actualizar estado a {}", id, estado);
        return pedidoService.actualizarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Pedido con id {} no encontrado para actualizar", id);
                    return ResponseEntity.status(404).build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable Long id) {
        log.info("DELETE /api/v1/pedidos/{} - Cancelar pedido", id);
        try {
            return pedidoService.cancelarPedido(id)
                    .map(p -> ResponseEntity.ok().body((Object)"Pedido cancelado correctamente"))
                    .orElseGet(() -> {
                        log.warn("Pedido con id {} no encontrado para cancelar", id);
                        return ResponseEntity.status(404).build();
                    });
        } catch (RuntimeException e) {
            log.error("Error al cancelar pedido {}: {}", id, e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}