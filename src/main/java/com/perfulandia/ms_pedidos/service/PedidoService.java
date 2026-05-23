package com.perfulandia.ms_pedidos.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ms_pedidos.dto.VentaDTO;
import com.perfulandia.ms_pedidos.model.Pedido;
import com.perfulandia.ms_pedidos.repository.PedidoRepository;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String MS_VENTAS_URL = "http://localhost:8085/api/v1/ventas";

    public List<Pedido> findAll() {
        log.info("Listando todos los pedidos");
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Long id) {
        log.info("Buscando pedido con id: {}", id);
        return pedidoRepository.findById(id);
    }

    public List<Pedido> findByIdCliente(Long idCliente) {
        log.info("Buscando pedidos del cliente: {}", idCliente);
        return pedidoRepository.findByIdCliente(idCliente);
    }

    public Pedido save(Pedido pedido) {
        log.info("Intentando crear pedido para cliente: {}", pedido.getIdCliente());
        try {
            VentaDTO ventaDTO = new VentaDTO(
                pedido.getIdPedido(),
                pedido.getIdCliente(),
                pedido.getIdSucursal(),
                pedido.getTotal(),
                "PENDIENTE"
            );
            restTemplate.postForObject(MS_VENTAS_URL, ventaDTO, String.class);
            pedido.setEstado("PENDIENTE");
            log.info("MS Ventas notificado correctamente");
        } catch (Exception e) {
            log.warn("MS Ventas no disponible: {}. Guardando en contingencia", e.getMessage());
            pedido.setEstado("PENDIENTE_SINCRONIZAR_VENTAS");
        }
        Pedido guardado = pedidoRepository.save(pedido);
        log.info("Pedido creado con id: {}", guardado.getIdPedido());
        return guardado;
    }

    public Optional<Pedido> actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del pedido {} a {}", id, nuevoEstado);
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        });
    }

    public Optional<Pedido> cancelarPedido(Long id) {
        log.info("Intentando cancelar pedido con id: {}", id);
        return pedidoRepository.findById(id).map(pedido -> {
            if (pedido.getEstado().equals("ENVIADO")) {
                log.warn("No se puede cancelar pedido {} porque está ENVIADO", id);
                throw new RuntimeException("No se puede cancelar un pedido que ya fue enviado");
            }
            pedido.setEstado("CANCELADO");
            log.info("Pedido {} cancelado correctamente", id);
            return pedidoRepository.save(pedido);
        });
    }
}