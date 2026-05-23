package com.perfulandia.ms_pedidos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.perfulandia.ms_pedidos.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Buscar pedidos por cliente
    List<Pedido> findByIdCliente(Long idCliente);
    
    // Buscar pedidos por estado
    List<Pedido> findByEstado(String estado);
}