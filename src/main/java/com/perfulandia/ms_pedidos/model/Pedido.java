package com.perfulandia.ms_pedidos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @NotNull(message = "El id del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El id de la sucursal es obligatorio")
    private Long idSucursal;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    private BigDecimal total;

    @NotBlank(message = "La direccion de envio es obligatoria")
    private String direccionEnvio;
}