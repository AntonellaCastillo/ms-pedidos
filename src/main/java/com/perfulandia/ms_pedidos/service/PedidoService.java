package com.perfulandia.ms_pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ms_pedidos.dto.VentaDTO;
import com.perfulandia.ms_pedidos.model.Pedido;
import com.perfulandia.ms_pedidos.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // URL de MS Ventas
    private static final String MS_VENTAS_URL = "http://localhost:8085/api/v1/ventas";

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

    // Crear pedido con resiliencia hacia MS Ventas
    public Pedido save(Pedido pedido) {
        try {
            // Construir DTO con datos relevantes para MS Ventas
            VentaDTO ventaDTO = new VentaDTO(
                pedido.getIdPedido(),
                pedido.getIdCliente(),
                pedido.getIdSucursal(),
                pedido.getTotal(),
                "PENDIENTE"
            );
            // Intentar notificar a MS Ventas
            restTemplate.postForObject(MS_VENTAS_URL, ventaDTO, String.class);
            pedido.setEstado("PENDIENTE");
        } catch (Exception e) {
            // Fallback — MS Ventas no disponible
            System.out.println("MS Ventas no disponible: " + e.getMessage());
            pedido.setEstado("PENDIENTE_SINCRONIZAR_VENTAS");
        }
        return pedidoRepository.save(pedido);
    }

    // Actualizar estado del pedido
    public Optional<Pedido> actualizarEstado(Long id, String nuevoEstado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        });
    }

    // Cancelar pedido — Regla de negocio KAN-63
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