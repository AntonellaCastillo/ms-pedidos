package com.perfulandia.ms_pedidos.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ms_pedidos.dto.VentaDTO;
import com.perfulandia.ms_pedidos.model.EstadoPedido;
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

        // Primero guardamos el pedido
        Pedido guardado = pedidoRepository.save(pedido);

        try {
            // Luego notificamos a MS Ventas con el id ya generado
            VentaDTO ventaDTO = new VentaDTO(
                guardado.getIdPedido(),
                guardado.getIdCliente(),
                guardado.getIdSucursal(),
                guardado.getTotal(),
                "PENDIENTE"
            );
            restTemplate.postForObject(MS_VENTAS_URL, ventaDTO, String.class);
            guardado.setEstado(EstadoPedido.PENDIENTE);
            pedidoRepository.save(guardado);
            log.info("MS Ventas notificado correctamente");
        } catch (Exception e) {
            log.warn("MS Ventas no disponible: {}. Guardando en contingencia", e.getMessage());
            guardado.setEstado(EstadoPedido.PENDIENTE_SINCRONIZAR_VENTAS);
            pedidoRepository.save(guardado);
        }

        log.info("Pedido creado con id: {}", guardado.getIdPedido());
        return guardado;
    }

    public Optional<Pedido> actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del pedido {} a {}", id, nuevoEstado);
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
            return pedidoRepository.save(pedido);
        });
    }

    public Optional<Pedido> cancelarPedido(Long id) {
        log.info("Intentando cancelar pedido con id: {}", id);
        return pedidoRepository.findById(id).map(pedido -> {
            if (pedido.getEstado().equals(EstadoPedido.ENVIADO)) {
                log.warn("No se puede cancelar pedido {} porque está ENVIADO", id);
                throw new RuntimeException("No se puede cancelar un pedido que ya fue enviado");
            }
            pedido.setEstado(EstadoPedido.CANCELADO);
            log.info("Pedido {} cancelado correctamente", id);
            return pedidoRepository.save(pedido);
        });
    }
}