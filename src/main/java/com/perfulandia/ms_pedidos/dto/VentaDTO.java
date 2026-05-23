package com.perfulandia.ms_pedidos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    private Long idPedido;
    private Long idCliente;
    private Long idSucursal;
    private Double total;
    private String estado;
}