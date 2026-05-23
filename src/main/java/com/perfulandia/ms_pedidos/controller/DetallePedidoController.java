package com.perfulandia.ms_pedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perfulandia.ms_pedidos.model.DetallePedido;
import com.perfulandia.ms_pedidos.service.DetallePedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/detalles")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    // GET — Listar detalles por pedido
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<?> getDetallesByPedido(@PathVariable Long idPedido) {
        List<DetallePedido> lista = detallePedidoService.findByIdPedido(idPedido);
        if (lista.isEmpty()) {
            return ResponseEntity.status(404).body("No hay detalles para ese pedido");
        }
        return ResponseEntity.ok(lista);
    }

    // POST — Agregar detalle a un pedido
    @PostMapping
    public ResponseEntity<?> postDetalle(@Valid @RequestBody DetallePedido detalle) {
        DetallePedido nuevo = detallePedidoService.save(detalle);
        return ResponseEntity.status(201).body(nuevo);
    }

    // DELETE — Eliminar detalle
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDetalle(@PathVariable Long id) {
        try {
            detallePedidoService.delete(id);
            return ResponseEntity.ok("Detalle eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Detalle no encontrado");
        }
    }
}