package com.perfulandia.ms_pedidos.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.ms_pedidos.model.DetallePedido;
import com.perfulandia.ms_pedidos.repository.DetallePedidoRepository;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    // Listar detalles por pedido
    public List<DetallePedido> findByIdPedido(Long idPedido) {
        return detallePedidoRepository.findByPedidoIdPedido(idPedido);
    }

    // Agregar detalle a un pedido
    public DetallePedido save(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    // Eliminar detalle
    public void delete(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}