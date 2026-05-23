# MS Pedidos - Perfulandia

Microservicio encargado de gestionar el ciclo de vida de los pedidos en el ecosistema Perfulandia.

## Información del microservicio
- **Puerto:** 8091
- **Base de datos:** db_perfulandia_pedidos
- **Tecnología:** Spring Boot 4.0.6, Java 25, MySQL

## Funcionalidades
- Crear pedido
- Consultar pedido por ID
- Listar pedidos por cliente
- Actualizar estado del pedido
- Cancelar pedido (no permitido si está ENVIADO)

## Endpoints
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/v1/pedidos | Crear pedido |
| GET | /api/v1/pedidos | Listar todos los pedidos |
| GET | /api/v1/pedidos/{id} | Buscar pedido por ID |
| GET | /api/v1/pedidos/cliente/{idCliente} | Listar pedidos por cliente |
| PUT | /api/v1/pedidos/{id}/estado | Actualizar estado |
| DELETE | /api/v1/pedidos/{id} | Cancelar pedido |

## Conexión con otros microservicios
- **MS Ventas (8085):** Notifica cuando se crea un pedido

## Cómo ejecutar
1. Tener XAMPP corriendo con MySQL
2. Crear la base de datos `db_perfulandia_pedidos`
3. Ejecutar el proyecto con `./mvnw spring-boot:run`