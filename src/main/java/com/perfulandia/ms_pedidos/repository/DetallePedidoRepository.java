package com.perfulandia.ms_pedidos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.perfulandia.ms_pedidos.model.DetallePedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
    // Buscar todos los detalles de un pedido específico
    List<DetallePedido> findByIdPedido(Long idPedido);
}
