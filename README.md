# Ecommerce - Marketplace API

Backend desarrollado en **Java + Spring Boot** para un marketplace de productos. Permite gestionar usuarios, categorías, productos y carritos de compra, con persistencia en **MySQL** mediante JPA/Hibernate. Incluye un frontend simple en HTML, CSS y JavaScript servido por el propio backend.

## Características

- CRUD completo de productos y categorías
- Gestión de usuarios con roles (`ROLE_ADMIN`, `ROLE_USER`)
- Carrito de compras por usuario: agregar, quitar y actualizar cantidades de productos, con control de stock
- Validaciones de datos con Bean Validation (`jakarta.validation`)
- Manejo centralizado de errores con `@RestControllerAdvice`
- Documentación interactiva de la API con Swagger / OpenAPI
- Frontend estático (HTML/CSS/JS) servido desde el mismo backend

## Tecnologías

- Java 22
- Spring Boot 4
- Spring Data JPA / Hibernate
- MySQL 8
- ModelMapper
- Lombok
- Maven
- Springdoc OpenAPI (Swagger UI)

## Requisitos previos

- JDK 22 instalado
- Maven (o usar el wrapper `mvnw` si está incluido)
- MySQL Server corriendo localmente (puerto 3306 por defecto)
- Un cliente para crear la base de datos (MySQL Workbench, DBeaver, o la terminal de MySQL)

## Instrucciones para ejecutar la aplicación

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/TU_REPO.git
cd TU_REPO
```

### 2. Crear la base de datos

Conectate a tu servidor MySQL y creá la base de datos vacía (las tablas se generan automáticamente al levantar la app):

```sql
CREATE DATABASE ecommerce;
```

### 3. Configurar las credenciales

La contraseña de la base de datos no está hardcodeada en el proyecto por seguridad. Se obtiene de una variable de entorno llamada `DB_PASSWORD`. Antes de correr la aplicación, definila con tu propia contraseña de MySQL:

**En PowerShell:**
```powershell
$env:DB_PASSWORD="tu_password_de_mysql"
```

**En IntelliJ IDEA:**
`Run` → `Edit Configurations...` → en `Environment variables` agregá `DB_PASSWORD=tu_password_de_mysql`.

Si tu usuario o URL de conexión son distintos a los configurados por defecto, podés ajustarlos en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

### 4. Ejecutar la aplicación

Desde la raíz del proyecto:

```bash
mvn spring-boot:run
```

O bien, abrí el proyecto en IntelliJ IDEA y ejecutá la clase `EcommerceApplication`.

La aplicación va a quedar disponible en:

```
http://localhost:8080
```

Al levantar el backend, vas a ver el frontend (HTML/CSS/JS ubicado en `src/main/resources/static`) servido directamente en la raíz `http://localhost:8080/`.

### 5. Datos iniciales

Al arrancar por primera vez, la aplicación crea automáticamente los roles base en la tabla `roles`:

- `ROLE_USER`
- `ROLE_ADMIN`

No hace falta cargarlos a mano.

## Documentación de la API (Swagger)

Con la aplicación corriendo, podés explorar todos los endpoints disponibles desde:

```
http://localhost:8080/swagger-ui.html
```

## Ejemplos de uso

### Crear una categoría

```http
POST /categorias
Content-Type: application/json

{
  "nombreCategoria": "Electrónica",
  "descripcionCategoria": "Productos electrónicos y gadgets"
}
```

### Crear un producto

```http
POST /productos
Content-Type: application/json

{
  "nombre": "Mouse inalámbrico",
  "descripcion": "Mouse ergonómico con batería recargable",
  "precio": 15000,
  "stock": 50,
  "categoriaId": 1,
  "imagenUrl": "https://ejemplo.com/mouse.jpg"
}
```

### Listar todos los productos

```http
GET /productos/lista
```

### Buscar productos por categoría

```http
GET /productos/categoria/id/1
```

### Crear un usuario

```http
POST /usuarios
Content-Type: application/json

{
  "nombre": "Tamara",
  "email": "tamara@example.com",
  "password": "12345678"
}
```

### Crear un carrito para un usuario

```http
POST /carritos?usuarioId=1
```

### Agregar un producto al carrito

```http
POST /carritos/1/productos/1?cantidad=2
```

### Ver el carrito de un usuario

```http
GET /carritos/usuario/1
```

### Actualizar la cantidad de un producto en el carrito

```http
PATCH /carritos/1/productos/1?cantidad=5
```

### Eliminar un producto del carrito

```http
DELETE /carritos/1/productos/1
```

## Estructura del proyecto

```
src/main/java/com/techlab/ecommerce/
├── configuration/      # CORS, ModelMapper, carga inicial de datos
├── controllers/api/    # Controllers REST
├── dtos/                # Objetos de transferencia de datos
├── entities/            # Entidades JPA
├── exceptions/          # Excepciones personalizadas y manejo global de errores
├── repositories/        # Repositorios Spring Data JPA
└── services/             # Lógica de negocio (interfaces + implementaciones)

src/main/resources/
├── static/               # Frontend (HTML, CSS, JS)
└── application.properties
```

