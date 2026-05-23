package com.perfulandia.ms_pedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perfulandia.ms_pedidos.model.Pedido;
import com.perfulandia.ms_pedidos.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // GET — Listar todos los pedidos
    @GetMapping
    public ResponseEntity<?> getPedidos() {
        List<Pedido> lista = pedidoService.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.status(404).body("No hay pedidos registrados");
        }
        return ResponseEntity.ok(lista);
    }

    // GET — Buscar pedido por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    // GET — Buscar pedidos por cliente
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> getPedidosByCliente(@PathVariable Long idCliente) {
        List<Pedido> lista = pedidoService.findByIdCliente(idCliente);
        if (lista.isEmpty()) {
            return ResponseEntity.status(404).body("No hay pedidos para ese cliente");
        }
        return ResponseEntity.ok(lista);
    }

    // POST — Crear pedido
    @PostMapping
    public ResponseEntity<?> postPedido(@Valid @RequestBody Pedido pedido) {
        Pedido nuevo = pedidoService.save(pedido);
        return ResponseEntity.status(201).body(nuevo);
    }

    // PUT — Actualizar estado del pedido
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> putEstadoPedido(@PathVariable Long id,
            @RequestParam String estado) {
        return pedidoService.actualizarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    // DELETE — Cancelar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable Long id) {
        try {
            return pedidoService.cancelarPedido(id)
                    .map(p -> ResponseEntity.ok().body((Object)"Pedido cancelado correctamente"))
                    .orElse(ResponseEntity.status(404).build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}