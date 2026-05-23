package com.perfulandia.ms_pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.ms_pedidos.model.Pedido;
import com.perfulandia.ms_pedidos.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Listar todos los pedidos
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    // Buscar pedido por id
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    // Buscar pedidos por cliente
    public List<Pedido> findByIdCliente(Long idCliente) {
        return pedidoRepository.findByIdCliente(idCliente);
    }

    // Crear pedido
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    // Actualizar estado del pedido
    public Optional<Pedido> actualizarEstado(Long id, String nuevoEstado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        });
    }

    // Cancelar pedido — Regla de negocio 
    public Optional<Pedido> cancelarPedido(Long id) {
        return pedidoRepository.findById(id).map(pedido -> {
            if (pedido.getEstado().equals("ENVIADO")) {
                throw new RuntimeException("No se puede cancelar un pedido que ya fue enviado");
            }
            pedido.setEstado("CANCELADO");
            return pedidoRepository.save(pedido);
        });
    }
}