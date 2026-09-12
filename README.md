# Sales Service

Microservicio Java 21 + Spring Boot para registrar ventas.

## Endpoints
- GET `/api/sales`
- GET `/api/sales/{id}`
- POST `/api/sales`
- GET `/actuator/health`

Ejemplo:
```json
{"productId":1,"productName":"Laptop Lenovo","quantity":2,"unitPrice":2499.90}
```

Puerto local: `8082`.
